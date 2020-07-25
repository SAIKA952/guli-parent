package com.example.msmservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"com.example"})
public class MsmApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsmApplication.class, args);
    }
}
