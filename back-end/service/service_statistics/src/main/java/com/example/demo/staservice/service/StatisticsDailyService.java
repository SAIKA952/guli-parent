package com.example.demo.staservice.service;

import com.example.demo.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-06
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    // 统计某一天的注册人数，生成统计数据
    void registerCount(String day);

    // 图表显示，返回两部分数据，日期json数组，数量json数组
    Map<String, Object> getShowData(String type, String begin, String end);
}
