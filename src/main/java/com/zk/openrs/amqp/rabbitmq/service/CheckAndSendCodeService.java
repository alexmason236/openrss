package com.zk.openrs.amqp.rabbitmq.service;

import com.zk.openrs.amqp.rabbitmq.sender.RabbitSender;
import com.zk.openrs.pojo.*;
import com.zk.openrs.service.ProductService;
import com.zk.openrs.service.UserService;
import com.zk.openrs.service.WxMessageService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CheckAndSendCodeService {
    @Resource
    private ProductService productService;
    @Resource
    private UserService userService;
    @Resource
    private WxMessageService wxMessageService;
    @Resource
    private RabbitSender rabbitSender;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional(rollbackFor = Exception.class)
    public void CheckAndSendCode(String code, ReceivedMobileData message, String categoryName, String productCurrentStatus) throws Exception {
        ProductInfo productInfo = productService.getProductByPhoneNumberAndProductNameAndProductStatus(message.getFromMobile(), categoryName, productCurrentStatus);
        if (productInfo != null) {
            Order order = productService.getOrderByProductNameAndOrderStatus(productInfo.getId(), OrderStatus.CREATED);
            if (order != null) {
                //TODO 通过微信发送验证码逻辑
                userService.updateUserAccPoint(-1 * order.getRentalTime(), order.getOpenId());
                productService.updateProductStatus(productInfo.getId(), ProductCurrentStatus.INUSE);
                productService.updateOrderStatus(order.getId(), OrderStatus.COMPLETE);
                wxMessageService.send(order.getOpenId(), categoryName, order.getRentalTime(), code);
                rabbitSender.sendWaitForOrderTTLMsg(order, order.getRentalTime() * 1000 * 60 * 60);
            }
        }
    }
}
