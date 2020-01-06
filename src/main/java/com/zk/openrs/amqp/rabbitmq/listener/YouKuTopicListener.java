package com.zk.openrs.amqp.rabbitmq.listener;

import com.zk.openrs.amqp.rabbitmq.RabbitMqConstant;
import com.zk.openrs.pojo.ReceivedMobileData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMqConstant.YOUKU_QUEUE)
public class YouKuTopicListener {
    @RabbitHandler
    public void process(ReceivedMobileData message) {
        System.out.println("Topic Receiver1 from youku  : " + message.getMessageContent());
    }
}
