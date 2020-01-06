package com.zk.openrs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.zk.openrs")
@EnableTransactionManagement
public class OpenrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenrsApplication.class, args);
    }

}
