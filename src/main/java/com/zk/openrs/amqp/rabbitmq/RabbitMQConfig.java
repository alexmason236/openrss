package com.zk.openrs.amqp.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {


    @Bean
    public Queue youKuQueue() {
        return new Queue(RabbitMQConstant.YOUKU_QUEUE);
    }

    @Bean
    public Queue baiDuQueue() {
        return new Queue(RabbitMQConstant.BADDU_QUEUE);
    }

    @Bean
    public Queue xunLeiQueue() {
        return new Queue(RabbitMQConstant.XUNLEI_QUEUE);
    }

    @Bean
    public Queue aiQiYiQueue() {
        return new Queue(RabbitMQConstant.AIQIYI_QUEUE);
    }

    @Bean
    public Queue uuKuQueue() {
        return new Queue(RabbitMQConstant.UU_QUEUE);
    }

    @Bean
    public Queue tengXunQueue() {
        return new Queue(RabbitMQConstant.TENGXUN_QUEUE);
    }

    @Bean
    public Queue tengXunTiYuQueue() {
        return new Queue(RabbitMQConstant.TENGXUNTIYU_QUEUE);
    }

    @Bean
    public Queue souHuQueue() {
        return new Queue(RabbitMQConstant.SOUHU_QUEUE);
    }

    @Bean
    public Queue leShiQueue() {
        return new Queue(RabbitMQConstant.LESHI_QUEUE);
    }

    @Bean
    public Queue othersQueue() {
        return new Queue(RabbitMQConstant.OTHERS_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitMQConstant.TOPIC_EXCHANGE);
    }

    @Bean
    public Binding bindingYouKuToExchange() {
        return BindingBuilder.bind(youKuQueue()).to(exchange()).with(RabbitMQConstant.YOUKU_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingBaiDuToExchange() {
        return BindingBuilder.bind(baiDuQueue()).to(exchange()).with(RabbitMQConstant.BADDU_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingXunLeiToExchange() {
        return BindingBuilder.bind(xunLeiQueue()).to(exchange()).with(RabbitMQConstant.XUNLEI_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingAQYToExchange() {
        return BindingBuilder.bind(aiQiYiQueue()).to(exchange()).with(RabbitMQConstant.AIQIYI_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingUUToExchange() {
        return BindingBuilder.bind(uuKuQueue()).to(exchange()).with(RabbitMQConstant.UU_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingTXToExchange() {
        return BindingBuilder.bind(tengXunQueue()).to(exchange()).with(RabbitMQConstant.TENGXUN_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingTXTYToExchange() {
        return BindingBuilder.bind(tengXunTiYuQueue()).to(exchange()).with(RabbitMQConstant.TENGXUNTIYU_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingSHToExchange() {
        return BindingBuilder.bind(souHuQueue()).to(exchange()).with(RabbitMQConstant.SOUHU_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingLSToExchange() {
        return BindingBuilder.bind(leShiQueue()).to(exchange()).with(RabbitMQConstant.LESHI_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingOthersToExchange() {
        return BindingBuilder.bind(othersQueue()).to(exchange()).with(RabbitMQConstant.OTHERS_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Queue immediateQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(RabbitMQConstant.DIRECT_CHECKCODE_QUEUE, true);
    }

    @Bean
    public Queue waitForTTLqueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(RabbitMQConstant.WAIT_FOR_TTL_QUEUE, true);
    }

    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitMQConstant.DELAYED_EXCHANGE_XDELAY, "x-delayed-message", true, false, args);
    }


    @Bean
    public Binding bindingNotify() {
        return BindingBuilder.bind(immediateQueue()).to(delayExchange()).with(RabbitMQConstant.DELAY_ROUTING_KEY_XDELAY).noargs();
    }

    @Bean
    public Binding bindingTTLNotify() {
        return BindingBuilder.bind(waitForTTLqueue()).to(delayExchange()).with(RabbitMQConstant.WAIT_FOR_TTL_QUEUE_ROUTE_KEY).noargs();
    }

}
