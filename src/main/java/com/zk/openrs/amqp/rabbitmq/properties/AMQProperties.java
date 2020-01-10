package com.zk.openrs.amqp.rabbitmq.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zk.rental")
@Data
public class AMQProperties {

    private int wait_for_code_time;

}
