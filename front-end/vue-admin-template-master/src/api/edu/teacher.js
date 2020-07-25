import request from '@/utils/request'

export default{

  // 1、讲师列表（条件分页查询）
  // current当前页 limit每页记录数 teacherQuery条件对象
  getTeacherListPage(current, limit, teacherQuery) {
    return request({
      // 两种url写法选一种用
      //   url: '/eduservice/edu-teacher/pageTeacherCondition/' + current + '/' + limit,
      url: `/eduservice/edu-teacher/pageTeacherCondition/${current}/${limit}`,
      method: 'post',
      // teacherQuery条件对象，后端使用的是RequestBody获取数据
      // data表示把对象转换成json传递到接口里面
      data: teacherQuery
    })
  },
  deleteTeacherById(id) { // 删除讲师
    return request({
      url: `/eduservice/edu-teacher/${id}`,
      method: 'delete'
    })
  },
  addTeacher(teacher) { // 添加讲师
    return request({
      url: `/eduservice/edu-teacher/addTeacher`,
      method: 'post',
      data: teacher // data表示把对象转换成json传递到接口里面
    })
  },
  getTeacherById(id) { // 根据id查询讲师
    return request({
      url: `/eduservice/edu-teacher/getTeacher/${id}`,
      method: 'get'
    })
  },
  updateTeacher(teacher) { // 修改讲师
    return request({
      url: `/eduservice/edu-teacher/updateTeacher`,
      method: 'post',
      data: teacher
    })
  }
}

