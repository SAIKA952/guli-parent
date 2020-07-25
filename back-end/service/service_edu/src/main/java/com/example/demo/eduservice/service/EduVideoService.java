package com.example.demo.eduservice.service;

import com.example.demo.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-22
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeVideoByCourseId(String courseId);
}
