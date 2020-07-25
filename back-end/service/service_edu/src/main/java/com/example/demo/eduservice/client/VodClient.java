package com.example.demo.eduservice.client;

import com.example.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod", fallback = VodFileDegradeFeignClient.class) // 调用的服务名称
@Component
public interface VodClient {
    //定义调用方法的路径
    // 根据视频id删除阿里云中视频
    @DeleteMapping("/eduvod/video/removeAliyunVideo/{id}") // 路径要写全路径
    public R removeAliyunVideo(@PathVariable("id") String id);

    // 删除多个阿里云视频的方法
    // 参数传递多个id
    @DeleteMapping("/eduvod/video/deleteBatch/")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
