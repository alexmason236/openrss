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
        return new Queue(RabbitMqConstant.YOUKU_QUEUE);
    }

    @Bean
    public Queue baiDuQueue() {
        return new Queue(RabbitMqConstant.BADDU_QUEUE);
    }

    @Bean
    public Queue xunLeiQueue() {
        return new Queue(RabbitMqConstant.XUNLEI_QUEUE);
    }

    @Bean
    public Queue aiQiYiQueue() {
        return new Queue(RabbitMqConstant.AIQIYI_QUEUE);
    }

    @Bean
    public Queue uuKuQueue() {
        return new Queue(RabbitMqConstant.UU_QUEUE);
    }

    @Bean
    public Queue tengXunQueue() {
        return new Queue(RabbitMqConstant.TENGXUN_QUEUE);
    }

    @Bean
    public Queue tengXunTiYuQueue() {
        return new Queue(RabbitMqConstant.TENGXUNTIYU_QUEUE);
    }

    @Bean
    public Queue souHuQueue() {
        return new Queue(RabbitMqConstant.SOUHU_QUEUE);
    }

    @Bean
    public Queue leShiQueue() {
        return new Queue(RabbitMqConstant.LESHI_QUEUE);
    }
    @Bean
    public Queue othersQueue() {
        return new Queue(RabbitMqConstant.OTHERS_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitMqConstant.TOPIC_EXCHANGE);
    }

    @Bean
    public Binding bindingYouKuToExchange() {
        return BindingBuilder.bind(youKuQueue()).to(exchange()).with(RabbitMqConstant.YOUKU_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingBaiDuToExchange() {
        return BindingBuilder.bind(baiDuQueue()).to(exchange()).with(RabbitMqConstant.BADDU_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingXunLeiToExchange() {
        return BindingBuilder.bind(xunLeiQueue()).to(exchange()).with(RabbitMqConstant.XUNLEI_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingAQYToExchange() {
        return BindingBuilder.bind(aiQiYiQueue()).to(exchange()).with(RabbitMqConstant.AIQIYI_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingUUToExchange() {
        return BindingBuilder.bind(uuKuQueue()).to(exchange()).with(RabbitMqConstant.UU_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingTXToExchange() {
        return BindingBuilder.bind(tengXunQueue()).to(exchange()).with(RabbitMqConstant.TENGXUN_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingTXTYToExchange() {
        return BindingBuilder.bind(tengXunTiYuQueue()).to(exchange()).with(RabbitMqConstant.TENGXUNTIYU_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingSHToExchange() {
        return BindingBuilder.bind(souHuQueue()).to(exchange()).with(RabbitMqConstant.SOUHU_QUEUE_ROUTE_KEY);
    }

    @Bean
    public Binding bindingLSToExchange() {
        return BindingBuilder.bind(leShiQueue()).to(exchange()).with(RabbitMqConstant.LESHI_QUEUE_ROUTE_KEY);
    }
    @Bean
    public Binding bindingOthersToExchange() {
        return BindingBuilder.bind(othersQueue()).to(exchange()).with(RabbitMqConstant.OTHERS_QUEUE_ROUTE_KEY);
    }
    @Bean
    public Queue immediateQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(RabbitMqConstant.DIRECT_CHECKCODE_QUEUE, true);
    }

    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitMqConstant.DELAYED_EXCHANGE_XDELAY, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding bindingNotify() {
        return BindingBuilder.bind(immediateQueue()).to(delayExchange()).with(RabbitMqConstant.DELAY_ROUTING_KEY_XDELAY).noargs();
    }

}
