package com.example.demo.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.eduservice.entity.EduSubject;
import com.example.demo.eduservice.entity.excel.SubjectData;
import com.example.demo.eduservice.service.EduSubjectService;
import com.example.exceptionhandler.GuliException;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    //因为SubjectExcelListener不能交给spring进行管理，需要自己new，不能注入其他对象
    public EduSubjectService subjectService;
    public SubjectExcelListener() {}
    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    //一行一行读取excel内容
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData == null){
            throw new GuliException(20001,"文件数据为空");
        }

        //因为是一行一行读取，所以每次读取有两个值，第一个值是一级分类，第二个是二级分类
        //判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(subjectData.getOneSubjectName(), subjectService);
        if(existOneSubject == null) { //如果没有相同的一级分类，就添加
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName()); //设置一级分类名称
            subjectService.save(existOneSubject);
        }

        //获取一级分类的id值，作为二级分类的pid
        String pid = existOneSubject.getId();

        //判断二级分类是否重复
        EduSubject existTwoSubject = this.existTwoSubject(subjectData.getTwoSubjectName(), subjectService, pid);
        if(existTwoSubject == null) {
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName()); //设置一级分类名称
            subjectService.save(existTwoSubject);
        }
    }

    //判断一级分类不能重复添加
    private EduSubject existOneSubject(String name, EduSubjectService subjectService){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", 0);
        EduSubject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }

    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(String name, EduSubjectService subjectService, String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);
        EduSubject twoSubject = subjectService.getOne(wrapper);
        return twoSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
