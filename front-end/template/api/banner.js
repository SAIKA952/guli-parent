import request from '@/utils/request'
export default {
  // 查询前两条banner的信息
  getListBanner() {
    return request({
      url: `/educms/bannerfront/getAllBanner`,
      method: 'get'
    })
  }
}