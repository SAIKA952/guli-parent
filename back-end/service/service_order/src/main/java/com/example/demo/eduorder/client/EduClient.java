package com.example.demo.eduorder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-edu")
public interface EduClient {
    // 根据课程id查询课程信息 --订单
    @PostMapping("/eduservice/coursefront/getCourseInfoOrder/{courseId}")
    public com.example.commonutils.ordervo.CourseWebVoOrder courseWebVoOrder(@PathVariable("courseId") String courseId);
}
