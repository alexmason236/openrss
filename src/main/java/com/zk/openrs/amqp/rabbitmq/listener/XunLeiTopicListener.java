package com.zk.openrs.amqp.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.zk.openrs.amqp.rabbitmq.RabbitMQConstant;
import com.zk.openrs.pojo.ReceivedMobileData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = RabbitMQConstant.XUNLEI_QUEUE)
public class XunLeiTopicListener {
    @RabbitHandler
    public void process(ReceivedMobileData message, Channel channel, @Headers Map<String, Object> headers) throws Exception {
        System.err.println("--------------------------------------");
        System.out.println("Topic Receiver1 from xunlei  : " + message.getMsgContent());
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag,false);
    }
}
