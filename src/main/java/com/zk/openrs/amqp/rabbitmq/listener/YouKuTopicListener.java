package com.zk.openrs.amqp.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.zk.openrs.amqp.rabbitmq.RabbitMQConstant;
import com.zk.openrs.amqp.rabbitmq.service.CheckAndSendCodeService;
import com.zk.openrs.pojo.*;
import com.zk.openrs.utils.MsgParser;
import com.zk.openrs.utils.parseImpl.YouKuMsgParserImpl;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitMQConstant.YOUKU_QUEUE)
public class YouKuTopicListener {

    @Resource
    private CheckAndSendCodeService checkAndSendCodeService;


    @RabbitHandler
    public void process(ReceivedMobileData message, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        MsgParser parser = new YouKuMsgParserImpl();

        System.err.println("--------------------------------------");
        System.out.println("Topic Receiver1 from youku  : " + message.getMsgContent());
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String code = parser.parseCode(message.getMsgContent());
        if (code != null) {
            try {
                checkAndSendCodeService.CheckAndSendCode(code, message, ProductNameConstant.YOUKU, ProductCurrentStatus.LOCKED);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                channel.basicAck(deliveryTag, false);
            }
        } else {
            channel.basicAck(deliveryTag, false);
        }
    }
}
