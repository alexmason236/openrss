package com.zk.openrs.utils;

import com.zk.openrs.amqp.rabbitmq.RabbitMQConstant;

public class ParseReceivedMobileMessageUtils {
    public static String parse(String content){
        if (content.contains("优酷")) return RabbitMQConstant.YOUKU_QUEUE_ROUTE_KEY;
        if (content.contains("迅雷")) return RabbitMQConstant.XUNLEI_QUEUE_ROUTE_KEY;
        if (content.contains("爱奇艺")) return RabbitMQConstant.AIQIYI_QUEUE_ROUTE_KEY;
        if (content.contains("UU")) return RabbitMQConstant.UU_QUEUE_ROUTE_KEY;
        if (content.contains("搜狐")) return RabbitMQConstant.SOUHU_QUEUE_ROUTE_KEY;
        if (content.contains("百度")) return RabbitMQConstant.BADDU_QUEUE_ROUTE_KEY;
        if (content.contains("腾讯体育")) return RabbitMQConstant.TENGXUNTIYU_QUEUE_ROUTE_KEY;
        if (content.contains("腾讯视频")) return RabbitMQConstant.TENGXUN_QUEUE_ROUTE_KEY;
        if (content.contains("乐视")) return RabbitMQConstant.LESHI_QUEUE_ROUTE_KEY;
        return RabbitMQConstant.OTHERS_QUEUE_ROUTE_KEY;
    }
}
