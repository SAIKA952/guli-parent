package com.example.demo.eduservice.client;

import com.example.demo.eduservice.entity.EduComment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Component
public class UcenterFileDegradeFeignClient implements UcenterClient {
    // 出错的时候会执行
    @Override
    public EduComment getUserInfo(String userId) { return null; }
}
