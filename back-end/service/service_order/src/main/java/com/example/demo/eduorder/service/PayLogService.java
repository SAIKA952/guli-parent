package com.example.demo.eduorder.service;

import com.example.demo.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-04
 */
public interface PayLogService extends IService<PayLog> {

    // 生成微信支付二维码
    Map createCode(String orderNo);

    // 根据订单号查询订单支付状态
    Map<String, String> queryPayStatus(String orderNo);

    // 想支付表里添加记录。更新订单装填
    void updateOrderStatus(Map<String, String> map);
}
