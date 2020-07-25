package com.example.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.example.commonutils.R;
import com.example.exceptionhandler.GuliException;
import com.example.vod.service.VodService;
import com.example.vod.utils.ConstantVodUtils;
import com.example.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    // 上传视频到阿里云
    @PostMapping("/uploadAliyunVideo")
    public R uploadAliyunVideo(MultipartFile file) {
        //返回上传视频id
        String videoId = vodService.uploadAliyun(file);
        return R.ok().data("videoId", videoId);
    }

    // 根据视频id删除阿里云中视频
    @DeleteMapping("/removeAliyunVideo/{id}")
    public R removeAliyunVideo(@PathVariable String id){
        try{
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频的request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(id);
            // 调用初始化对象的方法实现删除
            client.getAcsResponse(request);
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001, "删除视频失败");
        }
    }

    // 删除多个阿里云视频的方法
    // 参数传递多个id
    @DeleteMapping("/deleteBatch/")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList) {
        vodService.removeAliyunVideoBatch(videoIdList);
        return R.ok();
    }

    // 根据视频id获取视频播放凭证
    @GetMapping("/getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id) {
        try {
            // 创建初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            // 创建获取凭证的request对象和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            // 向request里设置视频id
            request.setVideoId(id);
            // 调用方法得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth", playAuth);
        }catch(Exception e) {
            throw new GuliException(20001, "获取凭证失败");
        }
    }

}
