package com.example.demo.eduorder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    // 生成订单的方法
    @PostMapping("/educenter/member/getUserInfoOrder/{userId}")
    public com.example.commonutils.ordervo.UcenterMemberOrder ucenterMemberOrder(@PathVariable("userId") String userId);
}
