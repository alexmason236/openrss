package com.zk.openrs.amqp.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.zk.openrs.amqp.rabbitmq.RabbitMqConstant;
import com.zk.openrs.pojo.ProductInfo;
import com.zk.openrs.pojo.ReceivedMobileData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = RabbitMqConstant.DIRECT_CHECKCODE_QUEUE)
public class XDelayListener {
    @RabbitHandler
    public void process(ProductInfo productInfo, Channel channel, @Headers Map<String, Object> headers) throws Exception {
        System.err.println("--------------------------------------");
        System.out.println("Topic Receiver1 from xdelayMsg  : " + productInfo.getId());
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag,false);
    }
}
