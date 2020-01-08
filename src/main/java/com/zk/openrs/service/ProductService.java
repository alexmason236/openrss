package com.zk.openrs.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.zk.openrs.mapper.ProductMapper;
import com.zk.openrs.pojo.Order;
import com.zk.openrs.pojo.OrderStatus;
import com.zk.openrs.pojo.ProductInfo;
import com.zk.openrs.pojo.SimpleResponse;
import com.zk.openrs.secuity.core.authentication.wechat.service.WechatUserDetails;
import com.zk.openrs.wechat.config.WxMaConfiguration;
import com.zk.openrs.wechat.config.WxMaProperties;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    @Resource
    private ProductMapper productMapper;
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

    public Order createOrder(String formId,int rentalTime, int  productId, Authentication authentication) throws ParseException {
        String openId=((WechatUserDetails)authentication.getPrincipal()).getUsername();
        Date date=df.parse(df.format(new Date()));
        Order order=new Order(productId,formId,rentalTime,openId,date, OrderStatus.CREATED);
        int orderId=productMapper.createOrder(order);
        order.setId(orderId);
        return order;
    }

    private List<ProductInfo> preCheckResource(String category){

    }

}
