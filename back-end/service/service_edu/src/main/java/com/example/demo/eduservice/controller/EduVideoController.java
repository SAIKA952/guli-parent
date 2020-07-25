package com.example.demo.eduservice.controller;


import com.example.commonutils.R;
import com.example.demo.eduservice.client.VodClient;
import com.example.demo.eduservice.entity.EduVideo;
import com.example.demo.eduservice.service.EduVideoService;
import com.example.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/eduservice/video")
//@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;

    @Autowired // 注入vodClient
    private VodClient vodClient;

    //1、添加小节
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }
    //2、删除小节
    //TODO: 后面这个方法需要完善，当删除小节的时候，同时删除里面的视屏
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        // 根据小节id得到到视频id，再进行删除
        EduVideo eduVideo = videoService.getById(id);
        String sourceId = eduVideo.getVideoSourceId();
        // 判断小节里面是否有视频id
        if(!StringUtils.isEmpty(sourceId)){
            // 根据视频id，远程调用实现视频删除
            R r = vodClient.removeAliyunVideo(sourceId);
            if(r.getCode() == 20001) {
                throw new GuliException(20001, "删除视频失败。。熔断器");
            }
        }
        // 删除小节
        videoService.removeById(id);
        return R.ok();
    }

    //3、修改小节 TODO
    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        videoService.updateById(eduVideo);
        return R.ok();
    }

    // 查询小节
    @GetMapping("/getVideo/{videoId}")
    public R getVideo(@PathVariable String videoId){
        EduVideo video  = videoService.getById(videoId);
        return R.ok().data("video", video);
    }
}

