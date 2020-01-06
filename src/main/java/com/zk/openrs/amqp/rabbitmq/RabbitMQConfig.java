package com.zk.openrs.amqp.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private static final String YOUKU_QUEUE_ROUTE_KEY="topic.youku";
    private static final String BADDU_QUEUE_ROUTE_KEY="topic.baidu";
    private static final String XUNLEI_QUEUE_ROUTE_KEY="topic.xunlei";
    private static final String AIQIYI_QUEUE_ROUTE_KEY="topic.aiqiyi";
    private static final String UU_QUEUE_ROUTE_KEY="topic.uu";
    private static final String TENGXUN_QUEUE_ROUTE_KEY="topic.tengxun";
    private static final String TENGXUNTIYU_QUEUE_ROUTE_KEY="topic.tengxuntiyu";
    private static final String SOUHU_QUEUE_ROUTE_KEY="topic.souhu";
    private static final String LESHI_QUEUE_ROUTE_KEY="topic.leshi";
    private static final String YOUKU_QUEUE="youku";
    private static final String BADDU_QUEUE="baidu";
    private static final String XUNLEI_QUEUE="xunlei";
    private static final String AIQIYI_QUEUE="aiqiyi";
    private static final String UU_QUEUE="uu";
    private static final String TENGXUN_QUEUE="tengxun";
    private static final String TENGXUNTIYU_QUEUE="tengxuntiyu";
    private static final String SOUHU_QUEUE="souhu";
    private static final String LESHI_QUEUE="leshi";

    @Bean
    public Queue youKuQueue(){
        return  new Queue(RabbitMQConfig.YOUKU_QUEUE);
    }
    @Bean
    public Queue baiDuQueue(){
        return  new Queue(RabbitMQConfig.BADDU_QUEUE);
    }
    @Bean
    public Queue xunLeiQueue(){
        return  new Queue(RabbitMQConfig.XUNLEI_QUEUE);
    }
    @Bean
    public Queue aiQiYiQueue(){
        return  new Queue(RabbitMQConfig.AIQIYI_QUEUE);
    }
    @Bean
    public Queue uuKuQueue(){
        return  new Queue(RabbitMQConfig.UU_QUEUE);
    }
    @Bean
    public Queue tengXunQueue(){
        return  new Queue(RabbitMQConfig.TENGXUN_QUEUE);
    }
    @Bean
    public Queue tengXunTiYuQueue(){
        return  new Queue(RabbitMQConfig.TENGXUNTIYU_QUEUE);
    }
    @Bean
    public Queue souHuQueue(){
        return  new Queue(RabbitMQConfig.SOUHU_QUEUE);
    }
    @Bean
    public Queue leShiQueue(){
        return  new Queue(RabbitMQConfig.LESHI_QUEUE);
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("exchange");
    }
    @Bean
    public Binding bindingYouKuToExchange(){
        return BindingBuilder.bind(youKuQueue()).to(exchange()).with(RabbitMQConfig.YOUKU_QUEUE_ROUTE_KEY);
    }
    @Bean
    public Binding bindingBaiDuToExchange(){
        return BindingBuilder.bind(baiDuQueue()).to(exchange()).with(RabbitMQConfig.BADDU_QUEUE_ROUTE_KEY);
    }
    @Bean
    public Binding bindingXunLeiToExchange(){
        return BindingBuilder.bind(xunLeiQueue()).to(exchange()).with(RabbitMQConfig.XUNLEI_QUEUE_ROUTE_KEY);
    }
    @Bean
    public Binding bindingAQYToExchange(){
        return BindingBuilder.bind(aiQiYiQueue()).to(exchange()).with(RabbitMQConfig.AIQIYI_QUEUE_ROUTE_KEY);
    }
    @Bean
    public Binding bindingUUToExchange(){
        return BindingBuilder.bind(uuKuQueue()).to(exchange()).with(RabbitMQConfig.UU_QUEUE_ROUTE_KEY);
    }
    @Bean
    public Binding bindingTXToExchange(){
        return BindingBuilder.bind(tengXunQueue()).to(exchange()).with(RabbitMQConfig.TENGXUN_QUEUE_ROUTE_KEY);
    }
    @Bean
    public Binding bindingTXTYToExchange(){
        return BindingBuilder.bind(tengXunTiYuQueue()).to(exchange()).with(RabbitMQConfig.TENGXUNTIYU_QUEUE_ROUTE_KEY);
    }
    @Bean
    public Binding bindingSHToExchange(){
        return BindingBuilder.bind(souHuQueue()).to(exchange()).with(RabbitMQConfig.SOUHU_QUEUE_ROUTE_KEY);
    }
    @Bean
    public Binding bindingLSToExchange(){
        return BindingBuilder.bind(leShiQueue()).to(exchange()).with(RabbitMQConfig.LESHI_QUEUE_ROUTE_KEY);
    }

}
