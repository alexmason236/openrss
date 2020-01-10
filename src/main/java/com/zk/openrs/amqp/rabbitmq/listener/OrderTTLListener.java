package com.zk.openrs.amqp.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.zk.openrs.amqp.rabbitmq.RabbitMQConstant;
import com.zk.openrs.pojo.Order;
import com.zk.openrs.pojo.ProductCurrentStatus;
import com.zk.openrs.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitMQConstant.WAIT_FOR_TTL_QUEUE)
public class OrderTTLListener {
    @Resource
    private ProductService productService;

    @RabbitHandler
    public void process(Order order, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        System.err.println("--------------------------------------");
        System.out.println("Topic Receiver1 from TTLDelay: " + order);
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        productService.updateProductStatus(order.getProductId(),ProductCurrentStatus.AVAILABLE);
        channel.basicAck(deliveryTag, false);
    }

}
