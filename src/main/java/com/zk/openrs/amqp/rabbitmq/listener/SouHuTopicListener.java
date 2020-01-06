package com.zk.openrs.amqp.rabbitmq.listener;

import com.zk.openrs.amqp.rabbitmq.RabbitMqConstant;
import com.zk.openrs.pojo.ReceivedMobileData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMqConstant.SOUHU_QUEUE)
public class SouHuTopicListener {
    @RabbitHandler
    public void process(ReceivedMobileData message) {
        System.out.println("Topic Receiver1 from souhu  : " + message.getMessageContent());
    }
}
