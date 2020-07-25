import request from '@/utils/request'

export default {
  // 生成订单的接口
  createOrders(courseId) {
    return request({
        url: `/eduorder/order/createdOrder/${courseId}`,
        method: 'post'
    })
  },
  // 根据订单id查询订单信息
  getOrdersInfo(orderId) {
    return request({
        url: `/eduorder/order/gerOrderInfo/${orderId}`,
        method: 'get'
    })
  },
  // 生成二维码的方法
  createCode(orderNo) {
    return request({
      url: `/eduorder/paylog/createCode/${orderNo}`,
      method: 'get'
    })
  },
  // 查询订单状态的方法
  queryPayStatus(orderNo) {
    return request({
      url: `/eduorder/paylog/queryPayStatus/${orderNo}`,
      method: 'get'
    })
  }
}