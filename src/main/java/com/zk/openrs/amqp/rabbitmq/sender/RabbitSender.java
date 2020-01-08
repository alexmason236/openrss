package com.zk.openrs.amqp.rabbitmq.sender;

import com.zk.openrs.amqp.rabbitmq.RabbitMqConstant;
import com.zk.openrs.pojo.Order;
import com.zk.openrs.pojo.ProductInfo;
import com.zk.openrs.pojo.ReceivedMobileData;
import com.zk.openrs.utils.ParseReceivedMobileMessageUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;


@Component
public class RabbitSender{
    @Autowired
    private RabbitTemplate rabbitTemplate;


    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("correlationData: " + correlationData);
            System.out.println("ack: " + ack);
            if(!ack){
                //可以进行日志记录、异常处理、补偿处理等
                System.err.println("异常处理....");
            }else {
                //更新数据库，可靠性投递机制
            }
        }
    };

    //回调函数: return返回
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText,
                                    String exchange, String routingKey) {
            System.err.println("return exchange: " + exchange + ", routingKey: "
                    + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
        }
    };

    //发送消息方法调用: 构建Message消息
    public void sendCodeGetedMsg(ReceivedMobileData receivedMobileData) throws Exception {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //id + 时间戳 全局唯一  用于ack保证唯一一条消息,这边做测试写死一个。但是在做补偿策略的时候，必须保证这是全局唯一的消息
        CorrelationData correlationData = new CorrelationData(String.valueOf(new Date().getTime()));
        rabbitTemplate.convertAndSend(RabbitMqConstant.TOPIC_EXCHANGE, ParseReceivedMobileMessageUtils.parse(receivedMobileData.getMessageContent()), receivedMobileData, correlationData);
    }
    public void sendWaitForCodedMsg(Order order, int delayTime) throws Exception {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //id + 时间戳 全局唯一  用于ack保证唯一一条消息,这边做测试写死一个。但是在做补偿策略的时候，必须保证这是全局唯一的消息
        CorrelationData correlationData = new CorrelationData(String.valueOf(new Date().getTime()));
        rabbitTemplate.convertAndSend(RabbitMqConstant.DELAYED_EXCHANGE_XDELAY, RabbitMqConstant.DELAY_ROUTING_KEY_XDELAY,order, message -> {
            message.getMessageProperties().setDelay(delayTime*1000);
            return message;
        },correlationData);
    }

}
