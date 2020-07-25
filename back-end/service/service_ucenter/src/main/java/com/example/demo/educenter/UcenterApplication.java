package com.example.demo.educenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient // nacos注册
@EnableFeignClients
@ComponentScan(basePackages = {"com.example"})
@MapperScan("com.example.demo.educenter.mapper")
public class UcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterApplication.class, args);
    }
}
