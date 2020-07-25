import request from '@/utils/request'

export default {
  // 分页课程查询
  getCourseList(page,limit,searchObj) {
    return request({
        url: `/eduservice/coursefront/getFrontCourseList/${page}/${limit}`,
        method: 'post',
        data: searchObj
    })
  },
  // 查询所有分类的方法
  getAllSubject() {
    return request({
      url: `/eduservice/edu-subject/getAllSubject`,
      method: 'get'
    })
  },
  // 课程详情方法
  getCourseInfo(courseId) {
    return request({
      url: `/eduservice/coursefront/getFrontCourseInfo/${courseId}`,
      method: 'get'
    })
  },
  // 模糊查询课程方法
  getCourseByNameVague(title) {
    return request({
      url: `/eduservice/coursefront/getCourseByNameVague/${title}`,
      method: 'get'
    })
  }
  
}