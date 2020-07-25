package com.example.demo.eduorder.service.impl;

import com.example.commonutils.ordervo.CourseWebVoOrder;
import com.example.commonutils.ordervo.UcenterMemberOrder;
import com.example.demo.eduorder.client.EduClient;
import com.example.demo.eduorder.client.UcenterClient;
import com.example.demo.eduorder.entity.Order;
import com.example.demo.eduorder.mapper.OrderMapper;
import com.example.demo.eduorder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.eduorder.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-04
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    // 生成订单的方法
    @Override
    public String createdOrders(String courseId, String userId) {
        // 通过远程调用，根据用户id获取用户信息
        UcenterMemberOrder userInfoOrder = ucenterClient.ucenterMemberOrder(userId);

        // 通过远程调用，根据课程id获取课程信息
        CourseWebVoOrder courseInfoOrder = eduClient.courseWebVoOrder(courseId);

        // 创建order对象，向order对象里设置需要的数据
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo()); // 订单号
        order.setCourseId(courseId); // 课程id
        order.setCourseTitle(courseInfoOrder.getTitle()); // 标题
        order.setCourseCover(courseInfoOrder.getCover()); // 封面
        order.setTeacherName(courseInfoOrder.getTeacherName()); // 讲师姓名
        order.setTotalFee(courseInfoOrder.getPrice()); // 价格
        order.setMemberId(userId); // 用户id
        order.setMobile(userInfoOrder.getMobile()); // 手机号
        order.setNickname(userInfoOrder.getNickname()); // 昵称
        order.setStatus(0); // 订单状态(0未支付，1已支付)
        order.setPayType(1); // 支付类型：微信
        baseMapper.insert(order);

        // 返回订单号
        return order.getOrderNo();
    }
}
