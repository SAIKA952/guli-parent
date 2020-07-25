package com.example.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    String uploadAliyun(MultipartFile file);

    // 删除多个阿里云视频的方法
    void removeAliyunVideoBatch(List videoIdList);
}
