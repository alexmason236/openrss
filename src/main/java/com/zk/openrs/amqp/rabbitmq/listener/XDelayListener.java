package com.zk.openrs.amqp.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.zk.openrs.amqp.rabbitmq.RabbitMQConstant;
import com.zk.openrs.pojo.*;
import com.zk.openrs.service.ProductService;
import com.zk.openrs.service.WxMessageService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitMQConstant.DIRECT_CHECKCODE_QUEUE)
public class XDelayListener {
    @Resource
    private ProductService productService;
    @Resource
    private WxMessageService wxMessageService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitHandler
    public void process(Order order, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        System.err.println("--------------------------------------");
        System.out.println("Topic Receiver1 from xdelayMsg  : " + order);

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            checkOrderAndUpdate(order);
        } catch (WxErrorException e) {
            logger.info(e.getError().getErrorMsg());
        } finally {
            channel.basicAck(deliveryTag, false);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void checkOrderAndUpdate(Order order) throws WxErrorException {
        Order checkOrder = productService.getOrderByOrderId(order.getId());
        if (!checkOrder.getCompleteFlag().equals(OrderStatus.COMPLETE)) {
            //TODO 释放绑定的资源
            productService.updateOrderStatus(checkOrder.getId(), OrderStatus.CANCELED);
            productService.updateProductStatus(order.getProductId(), ProductCurrentStatus.AVAILABLE);
            wxMessageService.sengFailMsg(order.getOpenId(), productService.getById(order.getId()).getProductBindAccount(),
                    String.valueOf(order.getRentalTime()), "此账号已被释放并退款，请重新购买");
        }
    }
}
