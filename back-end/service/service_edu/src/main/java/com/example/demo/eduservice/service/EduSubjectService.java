package com.example.demo.eduservice.service;

import com.example.demo.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-20
 */
public interface EduSubjectService extends IService<EduSubject> {
    //添加课程分类
    void saveSubject(MultipartFile file, EduSubjectService SubjectService);

    List<OneSubject> getAllOneTwoSubject();
}
