package com.example.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) // 不操作数据库
@EnableDiscoveryClient // nacos注册
@ComponentScan(basePackages = {"com.example"})
public class vodApplication {
    public static void main(String[] args) {
        SpringApplication.run(vodApplication.class, args);
    }
}
