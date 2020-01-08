package com.zk.openrs.amqp.rabbitmq.listener;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.rabbitmq.client.Channel;
import com.zk.openrs.amqp.rabbitmq.RabbitMqConstant;
import com.zk.openrs.pojo.*;
import com.zk.openrs.secuity.core.authentication.wechat.service.WechatUserDetails;
import com.zk.openrs.service.ProductService;
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
    @Autowired
    WxMaProperties properties;
    @RabbitHandler
    public void process(ReceivedMobileData message, Channel channel, @Headers Map<String, Object> headers) throws Exception {
        MsgParser parser=new YouKuMsgParserImpl();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.err.println("--------------------------------------");
        System.out.println("Topic Receiver1 from youku  : " + message.getMsgContent());
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        String code = parser.parseCode(message.getMsgContent());
        ProductInfo productInfo=productService.getProductByPhoneNumberAndProductNameAndProductStatus(message.getFromMobile(), ProductNameConstant.YOUKU, ProductCurrentStatus.BLOCKED);
        if(productInfo!=null){
            Order order=productService.getOrderByProductNameAndOrderStatus(productInfo.getId(),OrderStatus.CREATED);
            WxMaService wxService = WxMaConfiguration.getMaService(properties.getConfigs().get(0).getAppid());
            String openId= order.getOpenId();
            WxMaSubscribeMessage wxMaSubscribeMessage=new WxMaSubscribeMessage();
            wxMaSubscribeMessage.setToUser(openId);
            wxMaSubscribeMessage.setTemplateId(properties.getConfigs().get(0).getTemplate_id());
            wxMaSubscribeMessage.addData(new WxMaSubscribeMessage.Data("thing1",productInfo.getProductName()))
                    .addData(new WxMaSubscribeMessage.Data("amount2",code))
                    .addData(new WxMaSubscribeMessage.Data("date3",df.format(new Date())))
                    .addData(new WxMaSubscribeMessage.Data("thing4","你的验证码为"+code));
            wxService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
        }
        channel.basicAck(deliveryTag,false);


    }
}
