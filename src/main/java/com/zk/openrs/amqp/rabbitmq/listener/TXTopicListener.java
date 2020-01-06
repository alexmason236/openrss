package com.zk.openrs.amqp.rabbitmq.listener;

import com.zk.openrs.amqp.rabbitmq.RabbitMqConstant;
import com.zk.openrs.pojo.ReceivedMobileData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMqConstant.TENGXUN_QUEUE)
public class TXTopicListener {
    @RabbitHandler
    public void process(ReceivedMobileData message) {
        System.out.println("Topic Receiver1 from tengxun  : " + message.getMessageContent());
    }
}
