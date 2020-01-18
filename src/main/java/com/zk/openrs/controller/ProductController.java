package com.zk.openrs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.rabbitmq.client.impl.AMQBasicProperties;
import com.zk.openrs.amqp.rabbitmq.properties.AMQProperties;
import com.zk.openrs.amqp.rabbitmq.sender.RabbitSender;
import com.zk.openrs.pojo.*;
import com.zk.openrs.secuity.core.authentication.wechat.service.WechatUserDetails;
import com.zk.openrs.service.ProductService;
import com.zk.openrs.service.TransctionalService;
import com.zk.openrs.service.UserService;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
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
    @Autowired
    ObjectMapper objectMapper;
    @Resource
    TransctionalService transctionalService;

    @PostMapping("/addProduct")
    public SimpleResponse addProduct(@RequestParam("productName") String productName,
                                     @RequestParam("productBindAccount") String productBindAccount, @RequestParam("productBindPassword") String productBindPassword,
                                     @RequestParam("productCategoryId") int productCategoryId, Authentication authentication) {
        String openId = ((WechatUserDetails) authentication.getPrincipal()).getUsername();
        productService.addProduct(new ProductInfo(productName, productBindAccount, productBindPassword, ProductCurrentStatus.AVAILABLE, "getResAccessUrl", openId, productCategoryId));
        return new SimpleResponse("商品添加成功");
    }

    @PostMapping("addCategory")
    public SimpleResponse addCategory(@RequestParam("categoryName") String categoryName, @RequestParam("price") float price,
                                      @RequestParam("productBindPicture") MultipartFile categoryBindPicture) throws IOException {
        if (categoryBindPicture.isEmpty()) {
            return new SimpleResponse("请选择上传图片");
        }
        StorePath storePath = storageClient.uploadFile(categoryBindPicture.getInputStream(), categoryBindPicture.getSize(), FilenameUtils.getExtension(categoryBindPicture.getOriginalFilename()), null);
        logger.info("文件上传路径为: " + getResAccessUrl(storePath));
        productService.addCategory(new ProductCategory(categoryName, getResAccessUrl(storePath), price));
        return new SimpleResponse("类别添加成功");
    }

    @GetMapping("/getAllProduct")
    public List<ProductInfo> getAllProduct() {
        return productService.getAllProduct();
    }

    @GetMapping("/banner")
    public List<Banner> banners(){
        return productService.getBanner();
    }

    @PostMapping("/addBanner")
    public SimpleResponse addBanner(@RequestParam("bannerPic") MultipartFile bannerPic) throws IOException {
        if (bannerPic.isEmpty()) {
            return new SimpleResponse("请选择上传图片");
        }
        StorePath storePath = storageClient.uploadFile(bannerPic.getInputStream(), bannerPic.getSize(), FilenameUtils.getExtension(bannerPic.getOriginalFilename()), null);
        logger.info("文件上传路径为: " + getResAccessUrl(storePath));
        productService.addBanner(new Banner(getResAccessUrl(storePath),1));
        return new SimpleResponse("Banner添加成功");

    }

    @PostMapping("/buyProduct")
    public ExceptionResponse buyProduct(@RequestParam("formId") String formId, @RequestParam("postData") String buyPojoStr , Authentication authentication) throws JsonProcessingException {
        String openId = ((WechatUserDetails) authentication.getPrincipal()).getUsername();
        List<Map<String, Object>> buy=null;
        String respStr="购买请求以创建 ";
        List<BuyPojo> buyPojos = objectMapper.readValue(buyPojoStr, new TypeReference<List<BuyPojo>>() {
        });
        int costAcc=0;
        for (int i=0;i<buyPojos.size();i++){
            costAcc+=productService.getCategoryBiCid(buyPojos.get(i).getGoodsId()).get(0).getPrice()*buyPojos.get(i).getNumber();
        }
        if (userService.getByOpenId(openId).getAccPoint() < costAcc)
            return new ExceptionResponse(50050, "你的积分不够");

//        Map<String, Object> objectMap = productService.createOrder(formId, rentalTime, productName, authentication);
//        if (objectMap == null) return new ExceptionResponse(50100,"对不起，所有资源都在使用中，暂无可用的资源");
//        Order order = (Order) objectMap.get("order");
//        ProductInfo productInfo = (ProductInfo) objectMap.get("product");
        try {
            buy= transctionalService.doBuy(formId, buyPojos, authentication);
        } catch (Exception e) {
            return new ExceptionResponse(50001,e.getMessage() );
        }
        for (int i=0;i<buy.size();i++){
            rabbitSender.sendWaitForCodedMsg((Order) buy.get(i).get("order"), amqProperties.getWait_for_code_time());
        }
        return new ExceptionResponse(20020, "购买请求以创建,请务必于5分钟之内使用短信登陆,过期账号将释放 ");
    }



    @GetMapping("/getTestMsg")
    public void getTestMsg(ReceivedMobileData receivedMobileData) {
        System.out.println("收到安卓手机" + receivedMobileData.getFromMobile() + "发送过来的短信,短信内容为:" + receivedMobileData.getMsgContent());
        rabbitSender.sendCodeGetedMsg(receivedMobileData);
    }

    @GetMapping("/getAllCategory")
    public List<ProductCategory> getAllCategory() {
        return productService.getAllCategory();
    }

    @GetMapping("/getCategoryBiCid")
    public List<ProductCategory> getCategoryBiCid(@RequestParam("categoryId") int categoryId) {
        return productService.getCategoryBiCid(categoryId);
    }

//    @PostMapping("/testDelay")
//    public void testDelay(ProductInfo productInfo, int delayTime) throws Exception {
//        rabbitSender.sendWaitForCodedMsg(productInfo, delayTime);
//    }

    @GetMapping("/productAvailableCountAndPrice")
    public ProductCountAndPrice productAvailableCountAndPrice(int categoryId) {
        List<ProductInfo> productInfos = productService.getAvailableProductByCategoryId(categoryId);
        float price = productService.getProductCurrentPrice(categoryId);
        return new ProductCountAndPrice(productInfos.size(), price);
    }

    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = fdfsWebServer.getWebServerUrl() + "/" + storePath.getFullPath();
        return fileUrl;
    }


}
