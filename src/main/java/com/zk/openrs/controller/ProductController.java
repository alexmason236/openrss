package com.zk.openrs.controller;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zk.openrs.pojo.SimpleResponse;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private FdfsWebServer fdfsWebServer;
    @PostMapping("/add")
    public SimpleResponse addProduct(@RequestParam("productName") String productName,
                                     @RequestParam("productBindAccount") String productBindAccount,
                                     @RequestParam("productBindPassword") String productBindPassword,
                                     @RequestParam("productBindPicture") MultipartFile productBindPicture) throws IOException {
        if (productBindPicture.isEmpty()) {
            return new SimpleResponse("请选择上传图片");
        }
        StorePath storePath=storageClient.uploadFile(productBindPicture.getInputStream(),productBindPicture.getSize(), FilenameUtils.getExtension(productBindPicture.getOriginalFilename()),null);
        logger.info("文件上传路径为: "+getResAccessUrl(storePath));
        return new SimpleResponse(getResAccessUrl(storePath));
    }
    private String getResAccessUrl(StorePath storePath) {
         String fileUrl = fdfsWebServer.getWebServerUrl() +"/"+storePath.getFullPath();
         return fileUrl;
    }

}
