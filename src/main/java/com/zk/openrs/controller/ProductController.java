package com.zk.openrs.controller;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zk.openrs.amqp.rabbitmq.sender.RabbitSender;
import com.zk.openrs.pojo.*;
import com.zk.openrs.secuity.core.authentication.wechat.service.WechatUserDetails;
import com.zk.openrs.service.ProductService;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Data
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
    private RabbitSender rabbitSender;

    @PostMapping("/add")
    public SimpleResponse addProduct(@RequestParam("productName") String productName,
                                     @RequestParam("productBindAccount") String productBindAccount,
                                     @RequestParam("productBindPassword") String productBindPassword,
                                     @RequestParam("productBindPicture") MultipartFile productBindPicture,Authentication authentication) throws IOException {
        if (productBindPicture.isEmpty()) {
            return new SimpleResponse("请选择上传图片");
        }
        String openId=((WechatUserDetails)authentication.getPrincipal()).getUsername();
        StorePath storePath = storageClient.uploadFile(productBindPicture.getInputStream(), productBindPicture.getSize(), FilenameUtils.getExtension(productBindPicture.getOriginalFilename()), null);
        logger.info("文件上传路径为: " + getResAccessUrl(storePath));
        productService.addProduct(new ProductInfo(productName, productBindAccount, productBindPassword, ProductCurrentStatus.AVAILABLE, getResAccessUrl(storePath),openId));
        return new SimpleResponse(getResAccessUrl(storePath));
    }

    @GetMapping("/getAllProduct")
    public List<ProductInfo> getAllProduct() {
        return productService.getAllProduct();
    }

    @PostMapping("/buyProduct")
    public SimpleResponse buyProduct(@RequestParam("formId") String formId, @RequestParam("rentalTime") int rentalTime,
                                     @RequestParam("productName") String productName, Authentication authentication) throws Exception {
        Order order = productService.createOrder(formId, rentalTime, productName, authentication);
        if(order==null) return new SimpleResponse("对不起，所有资源都在使用中，暂无可用的资源");

        rabbitSender.sendWaitForCodedMsg(order,30);
        return new SimpleResponse("购买请求以创建，5分钟之内返回是否购买成功提醒");
    }

    @GetMapping("/getTestMsg")
    public void getTestMsg(ReceivedMobileData receivedMobileData) throws Exception {
        System.out.println("收到安卓手机"+receivedMobileData.getFromMobile()+"发送过来的短信,短信内容为:"+receivedMobileData.getMsgContent());
        rabbitSender.sendCodeGetedMsg(receivedMobileData);
    }

//    @PostMapping("/testDelay")
//    public void testDelay(ProductInfo productInfo, int delayTime) throws Exception {
//        rabbitSender.sendWaitForCodedMsg(productInfo, delayTime);
//    }

    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = fdfsWebServer.getWebServerUrl() + "/" + storePath.getFullPath();
        return fileUrl;
    }


}
