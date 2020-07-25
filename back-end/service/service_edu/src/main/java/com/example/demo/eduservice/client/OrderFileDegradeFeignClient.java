package com.example.demo.eduservice.client;

import org.springframework.stereotype.Component;


@Component
public class OrderFileDegradeFeignClient implements OrdersClient {
    // 出错的时候会执行
    @Override
    public boolean isBuyCourse(String courseId, String userId) {
        return false;
    }
}
