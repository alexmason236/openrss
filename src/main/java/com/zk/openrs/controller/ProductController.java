package com.zk.openrs.controller;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zk.openrs.amqp.rabbitmq.RabbitMQConfig;
import com.zk.openrs.amqp.rabbitmq.RabbitMqConstant;
import com.zk.openrs.pojo.ProductInfo;
import com.zk.openrs.pojo.PruductCurrentStatus;
import com.zk.openrs.pojo.ReceivedMobileData;
import com.zk.openrs.pojo.SimpleResponse;
import com.zk.openrs.service.ProductService;
import com.zk.openrs.wechat.utils.ParseReceivedMobileMessage;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private FdfsWebServer fdfsWebServer;
    @Resource
    private ProductService productService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @PostMapping("/add")
    public SimpleResponse addProduct(@RequestParam("productName") String productName,
                                     @RequestParam("productBindAccount") String productBindAccount,
                                     @RequestParam("productBindPassword") String productBindPassword,
                                     @RequestParam("productBindPicture") MultipartFile productBindPicture) throws IOException {
        if (productBindPicture.isEmpty()) {
            return new SimpleResponse("请选择上传图片");
        }
        StorePath storePath = storageClient.uploadFile(productBindPicture.getInputStream(), productBindPicture.getSize(), FilenameUtils.getExtension(productBindPicture.getOriginalFilename()), null);
        logger.info("文件上传路径为: " + getResAccessUrl(storePath));
        productService.addProduct(new ProductInfo(productName, productBindAccount, productBindPassword, PruductCurrentStatus.AVAILABLE, getResAccessUrl(storePath)));
        return new SimpleResponse(getResAccessUrl(storePath));
    }

    @GetMapping("/getAllProduct")
    public List<ProductInfo> getAllProduct() {
        return productService.getAllProduct();
    }
    @PostMapping("/buyProduct")
    public SimpleResponse buyProduct(@RequestParam("formId") String formId,@RequestParam("rentalTime") int rentalTime,
                                     @RequestParam("productId") int  productId, Authentication authentication) throws WxErrorException {
            System.out.println(formId+" "+rentalTime+" "+productId+" "+authentication);
            return  productService.buyProduct(formId,rentalTime,productId,authentication);
    }

    @PostMapping("/mobileSendMsg")
    public SimpleResponse getMobileSendMsg(ReceivedMobileData receivedMobileData){
        logger.info(receivedMobileData.toString());
        rabbitTemplate.convertAndSend(RabbitMqConstant.TOPIC_EXCHANGE, ParseReceivedMobileMessage.parse(receivedMobileData.getMessageContent()),receivedMobileData);
        return new SimpleResponse(receivedMobileData.toString());
    }

    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = fdfsWebServer.getWebServerUrl() + "/" + storePath.getFullPath();
        return fileUrl;
    }


}
