package com.example.demo.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.eduservice.entity.EduChapter;
import com.example.demo.eduservice.entity.EduVideo;
import com.example.demo.eduservice.entity.chapter.ChapterVo;
import com.example.demo.eduservice.entity.chapter.VideoVo;
import com.example.demo.eduservice.mapper.EduChapterMapper;
import com.example.demo.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.eduservice.service.EduVideoService;
import com.example.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-22
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService; // 注入小节service

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        // 1、根据课程id查询课程里所有的章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        // 2、根据课程id查询课程里所有的小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

        //创建list集合用于最终封装数据
        List<ChapterVo> finalList = new ArrayList<>();

        // 3、遍历查询章节list集合进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {
            // 得到每个章节
            EduChapter eduChapter = eduChapterList.get(i);
            // eduChapter对象赋值到ChapterVo里面
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            // 把chapterVo放到最终list集合
            finalList.add(chapterVo);

            // 创建集合，用于封装章节的小节
            List<VideoVo> videoList = new ArrayList<>();

            // 4、遍历查询小节list集合，进行封装
            for (int j = 0; j < eduVideoList.size(); j++) {
                // 得到每个小节
                EduVideo eduVideo = eduVideoList.get(j);
                // 判断小节里的chapterid和章节里的id是否相同
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    //相同，做封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    //放到小节封装的集合中
                    videoList.add(videoVo);
                }
            }
            // 把封装之后小节list集合，放到章节对象里
            chapterVo.setChildren(videoList);
        }
        return finalList;
    }

    //删除章节的方法，如果章节下有小节则不能删除，如果没有小节才能删除
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapterid查询小节表，如果有数据则不删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        //查询chapter_id = chapterId的数据有几条，也就是章节对应的小节数有几条
        int count = videoService.count(wrapper);
        if(count > 0){ // 查询出小节不进行删除
            throw new GuliException(20001, "不能删除");
        } else { // 有数据，不能删除
            int res = baseMapper.deleteById(chapterId);
            return res > 0;
        }
    }

    // 2、根据课程id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }
}
