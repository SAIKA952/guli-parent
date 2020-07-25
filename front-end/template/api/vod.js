import request from '@/utils/request'

export default {
  // 分页课程查询
  getPlayAuth(vid) {
    return request({
        url: `/eduvod/video/getPlayAuth/${vid}`,
        method: 'get'
    })
  }
}