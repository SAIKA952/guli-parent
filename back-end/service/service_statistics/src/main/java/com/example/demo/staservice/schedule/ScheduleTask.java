package com.example.demo.staservice.schedule;

import com.example.demo.staservice.service.StatisticsDailyService;
import com.example.demo.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleTask {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    // 每隔5秒执行一次方法
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1() {
//        System.out.println("--------task1执行了--------");
//    }

    // 在每天凌晨一点执行方法，把前一天的数据查询出来进行添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        // 获取当前日期的前一天
        statisticsDailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }
}
