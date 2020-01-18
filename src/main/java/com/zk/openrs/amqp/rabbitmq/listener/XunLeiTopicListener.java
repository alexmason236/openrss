package com.zk.openrs.amqp.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.zk.openrs.amqp.rabbitmq.RabbitMQConstant;
import com.zk.openrs.amqp.rabbitmq.service.CheckAndSendCodeService;
import com.zk.openrs.pojo.ProductCurrentStatus;
import com.zk.openrs.pojo.ProductNameConstant;
import com.zk.openrs.pojo.ReceivedMobileData;
import com.zk.openrs.utils.MsgParser;
import com.zk.openrs.utils.parseImpl.DefaultMsgParserImpl;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitMQConstant.XUNLEI_QUEUE)
public class XunLeiTopicListener {
    @Resource
    private CheckAndSendCodeService checkAndSendCodeService;


    @RabbitHandler
    public void process(ReceivedMobileData message, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        MsgParser parser = new DefaultMsgParserImpl();

        System.err.println("--------------------------------------");
        System.out.println("Topic Receiver1 from Xunlei  : " + message.getMsgContent());
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String code = parser.parseCode(message.getMsgContent());
        if (code != null) {
            checkAndSendCodeService.CheckAndSendCode(code, message, ProductNameConstant.XUNLEI, ProductCurrentStatus.LOCKED);
        }
        channel.basicAck(deliveryTag, false);
    }
}
