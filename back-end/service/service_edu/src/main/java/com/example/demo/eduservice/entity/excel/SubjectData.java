package com.example.demo.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

//创建和excel对应的实体类
@Data
public class SubjectData {
    @ExcelProperty(index = 0)
    private String oneSubjectName;

    @ExcelProperty(index = 1)
    private String twoSubjectName;
}
