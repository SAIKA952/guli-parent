package com.example.demo.staservice.controller;


import com.example.commonutils.R;
import com.example.demo.staservice.client.UcenterClient;
import com.example.demo.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/staservice/statisticsDaily")
//@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    // 统计某一天的注册人数，生成统计数据
    @PostMapping("/registerCount/{day}")
    public R registerCount(@PathVariable String day) {
        statisticsDailyService.registerCount(day);
        return R.ok();
    }

    // 图表显示，返回两部分数据，日期json数组，数量json数组
    @GetMapping("/showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type, @PathVariable String begin, @PathVariable String end) {
        Map<String, Object> map =  statisticsDailyService.getShowData(type, begin, end);
        return R.ok().data(map);
    }
}

