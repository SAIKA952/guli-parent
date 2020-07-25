package com.example.demo.eduservice.client;

import com.example.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-order", fallback = OrderFileDegradeFeignClient.class)
public interface OrdersClient {

    // 3、根据课程id和用户id查询订单表中的状态
    @GetMapping("/eduorder/order/isBuyCourse/{courseId}/{userId}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId, @PathVariable("userId") String userId);
}
