package com.zk.openrs.amqp.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.zk.openrs.amqp.rabbitmq.RabbitMQConstant;
import com.zk.openrs.pojo.Order;
import com.zk.openrs.pojo.ProductCurrentStatus;
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

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitMQConstant.WAIT_FOR_TTL_QUEUE)
public class OrderTTLListener {
    @Resource
    private ProductService productService;
    @Resource
    private WxMessageService wxMessageService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @RabbitHandler
    public void process(Order order, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        System.err.println("--------------------------------------");
        System.out.println("Topic Receiver1 from WAIT_FOR_TTL_QUEUE  : " + order);
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        productService.updateProductStatus(order.getProductId(),ProductCurrentStatus.AVAILABLE);
        logger.info(order+"订单使用时间到，释放资源,订单成功完成");
        try {
            wxMessageService.sendOrderCompleteMsg(order.getOpenId(),productService.getById(order.getProductId()).getProductBindAccount(),
                    String.valueOf(order.getRentalTime()),"使用时间到，账号已被释放，欢迎再次购买");
        } catch (WxErrorException e) {
            logger.info(e.getError().getErrorMsg());
        }
        channel.basicAck(deliveryTag, false);
    }

}
