package com.example.demo.eduservice.client;

import com.example.commonutils.R;
import com.example.commonutils.ordervo.UcenterMemberOrder;
import com.example.demo.eduservice.entity.EduComment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@FeignClient(name = "service-ucenter", fallback = UcenterFileDegradeFeignClient.class)
public interface UcenterClient {

    @GetMapping("/educenter/member/getUserInfo/{userId}")
    public EduComment getUserInfo(@PathVariable("userId") String userId);
}
