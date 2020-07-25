package com.example.demo.eduservice.entity.chapter;

import lombok.Data;

import java.util.*;

@Data
public class ChapterVo {
    private String id;
    private String title;
    //章节里表示小节
    List<VideoVo> children = new ArrayList<>();
}
