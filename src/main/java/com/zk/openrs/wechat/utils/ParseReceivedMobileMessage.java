package com.zk.openrs.wechat.utils;

import com.zk.openrs.amqp.rabbitmq.RabbitMqConstant;

public class ParseReceivedMobileMessage {
    public static String parse(String content){
        if (content.contains("优酷")) return RabbitMqConstant.YOUKU_QUEUE_ROUTE_KEY;
        if (content.contains("迅雷")) return RabbitMqConstant.XUNLEI_QUEUE_ROUTE_KEY;
        if (content.contains("爱奇艺")) return RabbitMqConstant.AIQIYI_QUEUE_ROUTE_KEY;
        if (content.contains("UU")) return RabbitMqConstant.UU_QUEUE_ROUTE_KEY;
        if (content.contains("搜狐")) return RabbitMqConstant.SOUHU_QUEUE_ROUTE_KEY;
        if (content.contains("百度")) return RabbitMqConstant.BADDU_QUEUE_ROUTE_KEY;
        if (content.contains("腾讯体育")) return RabbitMqConstant.TENGXUNTIYU_QUEUE_ROUTE_KEY;
        if (content.contains("腾讯视频")) return RabbitMqConstant.TENGXUN_QUEUE_ROUTE_KEY;
        if (content.contains("乐视")) return RabbitMqConstant.LESHI_QUEUE_ROUTE_KEY;
        return RabbitMqConstant.OTHERS_QUEUE_ROUTE_KEY;
    }
}
