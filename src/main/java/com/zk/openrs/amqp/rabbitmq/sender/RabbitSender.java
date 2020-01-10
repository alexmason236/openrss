package com.zk.openrs.amqp.rabbitmq.sender;

import com.zk.openrs.amqp.rabbitmq.RabbitMQConstant;
import com.zk.openrs.pojo.Order;
import com.zk.openrs.pojo.ReceivedMobileData;
import com.zk.openrs.utils.ParseReceivedMobileMessageUtils;
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
    public void sendCodeGetedMsg(ReceivedMobileData receivedMobileData) {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData(String.valueOf(new Date().getTime()));
        rabbitTemplate.convertAndSend(RabbitMQConstant.TOPIC_EXCHANGE, ParseReceivedMobileMessageUtils.parse(receivedMobileData.getMsgContent()), receivedMobileData, correlationData);
    }
    public void sendWaitForCodedMsg(Order order, int delayTime) {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData(String.valueOf(new Date().getTime()));
        rabbitTemplate.convertAndSend(RabbitMQConstant.DELAYED_EXCHANGE_XDELAY, RabbitMQConstant.DELAY_ROUTING_KEY_XDELAY,order, message -> {
            message.getMessageProperties().setDelay(delayTime*1000);
            return message;
        },correlationData);
    }

    public void sendWaitForOrderTTLMsg(Order order, int delayTime) {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData(String.valueOf(new Date().getTime()));
        rabbitTemplate.convertAndSend(RabbitMQConstant.DELAYED_EXCHANGE_XDELAY, RabbitMQConstant.WAIT_FOR_TTL_QUEUE_ROUTE_KEY,order, message -> {
            message.getMessageProperties().setDelay(delayTime*1000*60);
            return message;
        },correlationData);
    }

}
