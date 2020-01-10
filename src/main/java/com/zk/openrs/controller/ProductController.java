package com.zk.openrs.controller;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.rabbitmq.client.impl.AMQBasicProperties;
import com.zk.openrs.amqp.rabbitmq.properties.AMQProperties;
import com.zk.openrs.amqp.rabbitmq.sender.RabbitSender;
import com.zk.openrs.pojo.*;
import com.zk.openrs.secuity.core.authentication.wechat.service.WechatUserDetails;
import com.zk.openrs.service.ProductService;
import com.zk.openrs.service.UserService;
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
import java.util.Locale;
import java.util.Map;

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
    @Resource
    private AMQProperties amqProperties;
    @Resource
    private UserService userService;

    @PostMapping("/addProduct")
    public SimpleResponse addProduct(@RequestParam("productName") String productName,
                                     @RequestParam("productBindAccount") String productBindAccount, @RequestParam("productBindPassword") String productBindPassword,
                                     @RequestParam("productCategoryId") int productCategoryId, Authentication authentication) throws IOException {
        String openId = ((WechatUserDetails) authentication.getPrincipal()).getUsername();
        productService.addProduct(new ProductInfo(productName, productBindAccount, productBindPassword, ProductCurrentStatus.AVAILABLE, "getResAccessUrl", openId, productCategoryId));
        return new SimpleResponse("商品添加成功");
    }

    @PostMapping("addCategory")
    public SimpleResponse addCategory(@RequestParam("categoryName") String categoryName,
                                      @RequestParam("productBindPicture") MultipartFile categoryBindPicture) throws IOException {
        if (categoryBindPicture.isEmpty()) {
            return new SimpleResponse("请选择上传图片");
        }
        StorePath storePath = storageClient.uploadFile(categoryBindPicture.getInputStream(), categoryBindPicture.getSize(), FilenameUtils.getExtension(categoryBindPicture.getOriginalFilename()), null);
        logger.info("文件上传路径为: " + getResAccessUrl(storePath));
        productService.addCategory(new ProductCategory(categoryName, getResAccessUrl(storePath)));
        return new SimpleResponse("类别添加成功");
    }

    @GetMapping("/getAllProduct")
    public List<ProductInfo> getAllProduct() {
        return productService.getAllProduct();
    }

    @PostMapping("/buyProduct")
    public SimpleResponse buyProduct(@RequestParam("formId") String formId, @RequestParam("rentalTime") int rentalTime,
                                     @RequestParam("productName") String productName, Authentication authentication) throws Exception {
        String openId=((WechatUserDetails)authentication.getPrincipal()).getUsername();
        if (userService.getByOpenId(openId).getAccPoint()<rentalTime*1.0) return new SimpleResponse("对不起，你的积分不够");
        if (rentalTime==0){
            rentalTime=6;
        }else if(rentalTime==1){
            rentalTime=12;
        }else if (rentalTime==2){
            rentalTime=24;
        }else {
            rentalTime=168;
        }
        Map<String, Object> objectMap = productService.createOrder(formId, rentalTime, productName, authentication);
        if (objectMap == null) return new SimpleResponse("对不起，所有资源都在使用中，暂无可用的资源");
        Order order = (Order) objectMap.get("order");
        ProductInfo productInfo = (ProductInfo) objectMap.get("product");
        rabbitSender.sendWaitForCodedMsg(order, amqProperties.getWait_for_code_time());
        return new SimpleResponse("购买请求以创建，账号为 " + productInfo.getProductBindAccount() + ".请务必于5分钟之内使用短信登陆,过期账号将释放 ");
    }

    @GetMapping("/getTestMsg")
    public void getTestMsg(ReceivedMobileData receivedMobileData) throws Exception {
        System.out.println("收到安卓手机" + receivedMobileData.getFromMobile() + "发送过来的短信,短信内容为:" + receivedMobileData.getMsgContent());
        rabbitSender.sendCodeGetedMsg(receivedMobileData);
    }

    @GetMapping("/getAllCategory")
    public List<ProductCategory> getAllCategory() {
        return productService.getAllCategory();
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
