package com.zk.openrs.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.zk.openrs.mapper.ProductMapper;
import com.zk.openrs.pojo.*;
import com.zk.openrs.secuity.core.authentication.wechat.service.WechatUserDetails;
import com.zk.openrs.wechat.config.WxMaConfiguration;
import com.zk.openrs.wechat.config.WxMaProperties;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private UserService userService;
    @Autowired
    WxMaProperties properties;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public ProductInfo getById(int pid){
        return productMapper.getById(pid);
    }
    public void addProduct(ProductInfo productInfo){
        productMapper.addProduct(productInfo);
    }
    public List<ProductInfo> getAllProduct(){
        return productMapper.getAllProduct();
    }
    @Transactional(rollbackFor = Exception.class)
    public SimpleResponse buyProduct(String formId,int rentalTime, int  productId, Authentication authentication) throws WxErrorException {
        final WxMaService wxService = WxMaConfiguration.getMaService(properties.getConfigs().get(0).getAppid());
        String openId=((WechatUserDetails)authentication.getPrincipal()).getUsername();
        System.out.println("用户openID: "+openId);
        WxMaSubscribeMessage wxMaSubscribeMessage=new WxMaSubscribeMessage();
        wxMaSubscribeMessage.setToUser(openId);
        wxMaSubscribeMessage.setTemplateId(properties.getConfigs().get(0).getTemplate_id());
        wxMaSubscribeMessage.addData(new WxMaSubscribeMessage.Data("thing1","优酷会员"))
                .addData(new WxMaSubscribeMessage.Data("amount2","6.66"))
                .addData(new WxMaSubscribeMessage.Data("date3",df.format(new Date())))
                .addData(new WxMaSubscribeMessage.Data("thing4","用户名：test,密码:test"));
        wxService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
        return new SimpleResponse("购买成功，稍后请关注发送的消息");
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createOrder(String formId, int rentalTime, String productName, Authentication authentication) throws ParseException {
        List<ProductInfo> productInfos = preCheckResource(productName);
        if (productInfos.size()==0) return null;
        ProductInfo productInfo=productInfos.get(0);
        String openId=((WechatUserDetails)authentication.getPrincipal()).getUsername();
        userService.updateUserAccPoint(-1*rentalTime,openId);
        Date date=df.parse(df.format(new Date()));
        Order order=new Order(productInfo.getId(),formId,rentalTime,openId,date, OrderStatus.CREATED);
        productMapper.createOrder(order);
        int orderId=order.getId();
        System.out.println("插入后返回的ID是："+orderId);
        updateProductStatus(productInfo.getId(), ProductCurrentStatus.LOCKED);
        order.setId(orderId);
        Map<String ,Object> map=new HashMap<>();
        map.put("product",productInfo);
        map.put("order",order);
        return map;
    }

    private List<ProductInfo> preCheckResource(String productName){
        return productMapper.getProductByProductName(productName);
    }

    public ProductInfo getProductByPhoneNumberAndProductNameAndProductStatus(String fromMobile, String productName, String productStatus) {

        return productMapper.getProductByPhoneNumberAndProductNameAndProductStatus(fromMobile,productName,productStatus);
    }

    public Order getOrderByProductNameAndOrderStatus(int productId, String orderStatus) {
        return productMapper.getOrderByProductNameAndOrderStatus(productId,orderStatus);
    }

    public void updateProductStatus(int productId, String productStatus) {
        productMapper.updateProductStatus(productId,productStatus);
    }

    public void updateOrderStatus(int orderId, String orderStatus) {
        productMapper.updateOrderStatus(orderId,orderStatus);
    }

    public void addCategory(ProductCategory productCategory) {
        productMapper.addCategory(productCategory);
    }

    public List<ProductCategory> getAllCategory() {
        return productMapper.getAllCategory();
    }

    public Order getOrderByOrderId(int id) {
       return productMapper.getOrderByOrderId(id);
    }
}
