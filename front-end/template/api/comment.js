import request from '@/utils/request'

export default {
  // 分页查询评论
  getCourseComment(current, limit, courseId) {
    return request({
      url: `/eduservice/educomment/getCourseComment/${current}/${limit}/${courseId}`,
      method: 'post'
    })
  },
  // 添加评论的功能
  addCourseComment(courseId, content) {
    return request({
      url: `/eduservice/educomment/addCourseComment/${courseId}/${content}/`,
      method: 'post'
    })
  }
}