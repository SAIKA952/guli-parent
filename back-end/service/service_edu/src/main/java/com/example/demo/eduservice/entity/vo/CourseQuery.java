package com.example.demo.eduservice.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CourseQuery implements Serializable {
    private String title;
    private String status;
}
