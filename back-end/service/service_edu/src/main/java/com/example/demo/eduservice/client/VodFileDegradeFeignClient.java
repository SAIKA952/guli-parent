package com.example.demo.eduservice.client;

import com.example.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient {
    // 出错的时候会执行
    @Override
    public R removeAliyunVideo(String id) {
        return R.error().message("删除视频失败");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频失败");
    }
}
