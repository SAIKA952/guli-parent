package com.example.demo.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commonutils.R;
import com.example.demo.staservice.client.UcenterClient;
import com.example.demo.staservice.entity.StatisticsDaily;
import com.example.demo.staservice.mapper.StatisticsDailyMapper;
import com.example.demo.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-06
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    // 统计某一天的注册人数，生成统计数据
    @Override
    public void registerCount(String day) {
        // 添加之前先将原始数据删除，保证每次添加都是最新数据并且只有一条数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        baseMapper.delete(wrapper);

        // 远程调用得到某一天的注册人数
        R r = ucenterClient.countRegister(day);
        Integer count = (Integer) r.getData().get("count");

        // 把获取到的数据添加到数据库
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(count); // 注册人数
        sta.setDateCalculated(day); // 统计日期
        // 使用随机数先生成
        sta.setVideoViewNum(RandomUtils.nextInt(100, 200));
        sta.setLoginNum(RandomUtils.nextInt(100, 200));
        sta.setCourseNum(RandomUtils.nextInt(100, 200));
        baseMapper.insert(sta);
    }

    // 图表显示，返回两部分数据，日期json数组，数量json数组
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        // 根据条件查询对应的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        wrapper.select("date_calculated", type);
        List<StatisticsDaily> list = baseMapper.selectList(wrapper);

        // 因为返回的有两部分数据：日期和日期对应的数量
        // 前端要求数组json结构，对应后端java代码是list集合
        // 创建两个list，一个对应日期，一个对应数量
        List<String> dataCalculatedList = new ArrayList<>();
        List<Integer> numList = new ArrayList<>();

        // 遍历查询所有数据的list集合，进行封装
        for (int i = 0; i < list.size(); i++) {
            StatisticsDaily daily = list.get(i);
            dataCalculatedList.add(daily.getDateCalculated());

            // 判断类型
            switch(type) {
                case "login_num":
                    numList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numList.add(daily.getRegisterNum());
                case "video_view_num":
                    numList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        // 把封装之后的两个list集合放到map里返回
        Map<String, Object> map = new HashMap<>();
        map.put("date_calculated", dataCalculatedList);
        map.put("numList", numList);
        return map;
    }
}
