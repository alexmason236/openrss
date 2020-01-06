package com.zk.openrs.amqp.rabbitmq.listener;

import com.zk.openrs.amqp.rabbitmq.RabbitMqConstant;
import com.zk.openrs.pojo.ReceivedMobileData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMqConstant.UU_QUEUE)
public class UUTopicListener {
    @RabbitHandler
    public void process(ReceivedMobileData message) {
        System.out.println("Topic Receiver1 from uu  : " + message.getMessageContent());
    }
}
