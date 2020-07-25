import request from '@/utils/request'

export default{

  // 1、添加课程信息功能
  addCourseInfo(courseInfo) {
    return request({
      url: `/eduservice/course/addCourseInfo`,
      method: 'post',
      data: courseInfo
    })
  },
  // 2、查询所有讲师
  getTeacher() {
    return request({
      url: `/eduservice/edu-teacher/findAll`,
      method: 'get'
    })
  },
  // 根据课程id查询课程基本信息
  getCourseInfoId(courseId) {
    return request({
      url: `/eduservice/course/getCourseInfo/${courseId}`,
      method: 'get'
    })
  },
  // 修改课程信息
  updateCourseInfo(courseInfo) {
    return request({
      url: `/eduservice/course/updateCourseInfo`,
      method: 'post',
      data: courseInfo
    })
  },
  // 课程确认信息显示
  getPublishCourseInfo(id) {
    return request({
      url: `/eduservice/course/getPublishCourseInfo/${id}`,
      method: 'get'
    })
  },
  // 课程最终发布
  publishCourse(id) {
    return request({
      url: `/eduservice/course/publishCourse/${id}`,
      method: 'post'
    })
  },
  // TODO 课程列表
  getListCourse(current, limit, courseQuery) {
    return request({
      url: `/eduservice/course/pageCourseCondition/${current}/${limit}`,
      method: 'post',
      data: courseQuery
    })
  }
}

