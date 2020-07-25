package com.example.demo.eduservice.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.eduservice.client.VodClient;
import com.example.demo.eduservice.entity.EduCourse;
import com.example.demo.eduservice.entity.EduVideo;
import com.example.demo.eduservice.mapper.EduVideoMapper;
import com.example.demo.eduservice.service.EduCourseService;
import com.example.demo.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-22
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    // 注入vodClient
    @Autowired
    private VodClient vodClient;

    // 1、根据课程id删除小节
    // TODO 删除小节的时候删除对应的视频文件
    @Override
    public void removeVideoByCourseId(String courseId) {
        // 1、根据课程id查询课程所有的视频id
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        wrapperVideo.select("video_source_id"); // 只查询video_source_id字段
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);

        // 将list<EduVideo> 转换成 list<String>
        List<String> videoIds = new ArrayList<>();
        for(int i = 0; i < eduVideoList.size(); i++) {
            EduVideo eduVideo = eduVideoList.get(i);
            String VideoSourceId = eduVideo.getVideoSourceId();
            if(!StringUtils.isEmpty(VideoSourceId)) {
                // 放到videoIds集合中
                videoIds.add(VideoSourceId);
            }
        }

        if(videoIds.size() > 0) {
            // 根据多个视频id删除视频
            vodClient.deleteBatch(videoIds);
        }

        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }
}
