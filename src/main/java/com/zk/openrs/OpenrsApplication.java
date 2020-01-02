package com.zk.openrs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zk.openrs")
public class OpenrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenrsApplication.class, args);
    }

}
