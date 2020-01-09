package com.zk.openrs.amqp.rabbitmq.listener;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.rabbitmq.client.Channel;
import com.zk.openrs.amqp.rabbitmq.RabbitMqConstant;
import com.zk.openrs.pojo.*;
import com.zk.openrs.secuity.core.authentication.wechat.service.WechatUserDetails;
import com.zk.openrs.service.ProductService;
import com.zk.openrs.service.UserService;
import com.zk.openrs.utils.MsgParser;
import com.zk.openrs.utils.parseImpl.YouKuMsgParserImpl;
import com.zk.openrs.wechat.config.WxMaConfiguration;
import com.zk.openrs.wechat.config.WxMaProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitMqConstant.YOUKU_QUEUE)
public class YouKuTopicListener {
    @Resource
    private ProductService productService;
    @Resource
    private UserService userService;

    @RabbitHandler
    public void process(ReceivedMobileData message, Channel channel, @Headers Map<String, Object> headers) throws Exception {
        MsgParser parser=new YouKuMsgParserImpl();

        System.err.println("--------------------------------------");
        System.out.println("Topic Receiver1 from youku  : " + message.getMsgContent());
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        String code = parser.parseCode(message.getMsgContent());
        ProductInfo productInfo=productService.getProductByPhoneNumberAndProductNameAndProductStatus(message.getFromMobile(), ProductNameConstant.YOUKU, ProductCurrentStatus.LOCKED);
        if(productInfo!=null){
            Order order=productService.getOrderByProductNameAndOrderStatus(productInfo.getId(),OrderStatus.CREATED);
            if(order!=null){
                //TODO 通过微信发送验证码逻辑
                userService.updateUserAccPoint(-1*order.getRentalTime(),order.getOpenId());
                productService.updateProductStatus(productInfo.getId(),ProductCurrentStatus.INUSE);
                productService.updateOrderStatus(order.getId(),OrderStatus.COMPLETE);
            }
            System.out.println("获取到的CODE是："+code);
        }
        channel.basicAck(deliveryTag,false);
    }
}
