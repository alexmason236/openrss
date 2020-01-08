package com.zk.openrs.amqp.rabbitmq;

public interface RabbitMqConstant {
    String YOUKU_QUEUE_ROUTE_KEY = "topic.youku";
    String BADDU_QUEUE_ROUTE_KEY = "topic.baidu";
    String XUNLEI_QUEUE_ROUTE_KEY = "topic.xunlei";
    String AIQIYI_QUEUE_ROUTE_KEY = "topic.aiqiyi";
    String UU_QUEUE_ROUTE_KEY = "topic.uu";
    String TENGXUN_QUEUE_ROUTE_KEY = "topic.tengxun";
    String TENGXUNTIYU_QUEUE_ROUTE_KEY = "topic.tengxuntiyu";
    String SOUHU_QUEUE_ROUTE_KEY = "topic.souhu";
    String LESHI_QUEUE_ROUTE_KEY = "topic.leshi";
    String OTHERS_QUEUE_ROUTE_KEY = "topic.others";
    String YOUKU_QUEUE = "youku";
    String BADDU_QUEUE = "baidu";
    String XUNLEI_QUEUE = "xunlei";
    String AIQIYI_QUEUE = "aiqiyi";
    String UU_QUEUE = "uu";
    String TENGXUN_QUEUE = "tengxun";
    String TENGXUNTIYU_QUEUE = "tengxuntiyu";
    String SOUHU_QUEUE = "souhu";
    String LESHI_QUEUE = "leshi";
    String OTHERS_QUEUE="others";
    String TOPIC_EXCHANGE="exchange";
    String DIRECT_CHECKCODE_QUEUE="checkcode";
    String DELAYED_EXCHANGE_XDELAY="check_code_xdelay";
    String DELAY_ROUTING_KEY_XDELAY="delay.xdelay";
}
