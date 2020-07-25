package com.example.demo.eduorder.service;

import com.example.demo.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-04
 */
public interface OrderService extends IService<Order> {

    // 生成订单的方法
    String createdOrders(String courseId, String userId);
}
