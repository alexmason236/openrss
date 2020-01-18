package com.zk.openrs.controller;

import com.zk.openrs.amqp.rabbitmq.properties.AMQProperties;
import com.zk.openrs.pojo.Order;
import com.zk.openrs.pojo.ProductInfo;
import com.zk.openrs.secuity.core.authentication.wechat.service.WechatUserDetails;
import com.zk.openrs.service.ProductService;
import com.zk.openrs.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Resource
    private ProductService productService;
    @Resource
    private AMQProperties amqProperties;
    @Resource
    private UserService userService;
    @GetMapping("/me")
    public Map<String, Object> getAuth(Authentication authentication) {
        WechatUserDetails wechatUserDetails = ((WechatUserDetails) authentication.getPrincipal());
        String openId = wechatUserDetails.getUsername();
        Map<String, Object> map = new HashMap<>();
        List<Order> orders = productService.getOrdersByUser(openId);
        List<Order> collect = orders.stream().map(order -> {
            ProductInfo productInfo = productService.getById(order.getProductId());
            int cid = productInfo.getProductCategoryId();
            String productBindAccount = productInfo.getProductBindAccount();
            String bindPicPath = productService.getCategoryBiCid(cid).get(0).getCategoryBindPicturePath();
            order.setBindPicPath(bindPicPath);
            order.setBindAccount(productBindAccount);
            order.setExpire_in(amqProperties.getWait_for_code_time()-new Date().getTime()/1000+order.getCreateTime().getTime()/1000);
            return order;
        }).collect(Collectors.toList());
        Collections.reverse(collect);
        map.put("code", 20000);
        map.put("orders", collect);
        map.put("balance",userService.getByOpenId(openId).getAccPoint());
        return map;
    }
}
