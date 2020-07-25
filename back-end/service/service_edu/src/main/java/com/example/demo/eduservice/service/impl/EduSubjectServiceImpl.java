package com.example.demo.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.eduservice.entity.EduSubject;
import com.example.demo.eduservice.entity.excel.SubjectData;
import com.example.demo.eduservice.entity.subject.OneSubject;
import com.example.demo.eduservice.entity.subject.TwoSubject;
import com.example.demo.eduservice.listener.SubjectExcelListener;
import com.example.demo.eduservice.mapper.EduSubjectMapper;
import com.example.demo.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-20
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService SubjectService) {
        try{
            //文件输入流
            InputStream inputStream = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(inputStream, SubjectData.class, new SubjectExcelListener(SubjectService)).sheet().doRead();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //课程分类列表（树形）
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //1、查询所有一级分类 parentid = 0
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", "0");

        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        //2、查询所有二级分类 parentid != 0
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id", "0");

        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //创建list集合，用于存储最终封装数据
        List<OneSubject> finalSubjectList = new ArrayList<>();

        //3、封装一级分类
        //List<EduSubject>转换为List<OneSubject>
        //做法：查询出来所有的一级分类list集合进行比那里，得到每一个一级分类对象，
        //获取每一个一级分类对象值，封装到要求的list集合里面List<OneSubject> finalSubjectList
        for(int i = 0; i < oneSubjectList.size(); i++) { //遍历oneSubjectList集合
            //得到oneSubjectList每个eduSubject对象
            EduSubject eduSubject = oneSubjectList.get(i);

            //把eduSubject里面的值获取出来，放到OneSubject对象里面
            OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());
            //可以使用一个工具类来代替上面两句代码
            //eduSubject的值复制到对应的oneSubject对象里
            BeanUtils.copyProperties(eduSubject, oneSubject);
            //多个OneSubject放到finalSubjectList里面
            finalSubjectList.add(oneSubject);

            //在一级分类循环遍历查询所有的二级分类
            //创建list集合，封装每一个一级分类的二级分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            //遍历二级分类list集合
            for (int j = 0; j < twoSubjectList.size(); j++) {
                //获取每一个二级分类
                EduSubject tSubject = twoSubjectList.get(j);
                //判断二级分类的parentid和一级分类的id是否一样
                if(tSubject.getParentId().equals(eduSubject.getId())) {
                    //4、封装二级分类
                    //把tSubject值复制到TwoSubject里面，放到twoFinalSubjectList里面
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject, twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }

            //把一级下面所有的二级分类放到一级分类里面
            oneSubject.setChildren(twoFinalSubjectList);
        }
        return finalSubjectList;
    }
}
