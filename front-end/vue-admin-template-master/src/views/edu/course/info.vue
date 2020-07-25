<template>

  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="最终发布"/>
    </el-steps>

    <el-form label-width="120px">

      <el-form-item label="课程标题">
        <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"/>
      </el-form-item>

      <!-- 所属分类 TODO -->
      <!-- 课程分类 -->
      <!-- @change：当域对象的数据发生改变就会触发 -->
      <el-form-item label="课程分类">
        <el-select
          v-model="courseInfo.subjectParentId"
          placeholder="一级分类"
          @change="subjectLevelOneChanged">
          <!-- label是显示的名称，value是实际传的值，key是唯一标识
          这里提交的时候是提交课程的id -->
          <el-option
            v-for="subject in subjectOneList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"/>
        </el-select>

        <!-- 二级分类 -->
        <el-select v-model="courseInfo.subjectId" placeholder="二级分类">
          <el-option
            v-for="subject in subjectTwoList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"/>
        </el-select>

      </el-form-item>

      <!-- 课程讲师 TODO -->
      <!-- 课程讲师 -->
      <el-form-item label="课程讲师">
        <el-select
          v-model="courseInfo.teacherId"
          placeholder="请选择">
          <!-- label是显示的名称，value是实际传的值，key是唯一标识
          这里提交的时候是提交教师的id -->
          <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="总课时">
        <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数"/>
      </el-form-item>

      <!-- 课程简介 TODO -->
      <!-- 课程简介-->
      <el-form-item label="课程简介">
        <tinymce :height="300" v-model="courseInfo.description"/>
      </el-form-item>

      <!-- 课程封面 TODO -->
      <!-- 课程封面-->
      <el-form-item label="课程封面">
        <!-- show-file-list：显示文件列表
             on-success：上传成功
             before-upload：上传之前 -->
        <el-upload
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :action="BASE_API+'/eduoss/fileoss'"
          class="avatar-uploader">
          <img :src="courseInfo.cover">
        </el-upload>

      </el-form-item>

      <el-form-item label="课程价格">
        <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元"/> 元
      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存并下一步</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import course from '@/api/edu/course'
import subject from '@/api/edu/subject'
import Tinymce from '@/components/Tinymce'

export default {
  components: { Tinymce }, // 声明富文本组件
  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用
      courseInfo: {
        title: '',
        subjectId: '', // 二级分类的id
        subjectParentId: '', // 一级分类的id
        teacherId: '',
        lessonNum: 0,
        description: '',
        cover: 'https://edu-952.oss-cn-hangzhou.aliyuncs.com/yukino.png', // 头像
        price: 0
      },
      courseId: '',
      BASE_API: process.env.BASE_API, // 接口API地址
      teacherList: [], // 封装所有讲师数据
      subjectOneList: [], // 一级分类
      subjectTwoList: [] // 二级分类
    }
  },

  watch: { // 监听
    $route(to, from) { // 路由变化方式，路由发生变化，方法就会执行
      this.courseInfo = {}
      this.courseInfo.cover = 'https://edu-952.oss-cn-hangzhou.aliyuncs.com/yukino.png'
    }
  },

  created() {
    // 获取路由id值
    if (this.$route.params && this.$route.params.id) {
      this.courseId = this.$route.params.id
      this.getCourseInfo() // 调用根据id查询课程的方法
    } else {
      // 初始化所有讲师
      this.getListTeacher()
      // 初始化一级分类
      this.getOneSubject()
    }
  },

  methods: {
    // 根据课程id查询信息
    getCourseInfo() {
      course.getCourseInfoId(this.courseId)
        .then(response => {
          // 在courseInfo中有课程的基本信息，包含一级分类id和二级分类id
          this.courseInfo = response.data.courseInfoVo
          // 1、查询所有的分类，包含一级和二级
          subject.getSubjectList()
            .then(response => {
            // 获取所有一级分类
              this.subjectOneList = response.data.list

              // 把所有的一级分类数组进行遍历，比较当前courseInfo里面一级分类id和所有的一级分类id
              for (var i = 0; i < this.subjectOneList.length; i++) {
                // 获取每个一级分类
                var oneSubject = this.subjectOneList[i]
                if (this.courseInfo.subjectParentId === oneSubject.id) {
                  // 获取一级分类中的所有二级分类
                  this.subjectTwoList = oneSubject.children
                }
              }
            })
          // 初始化所有讲师
          this.getListTeacher()
        })
    },
    // 上传封面成功调用的方法
    handleAvatarSuccess(res, file) {
      this.courseInfo.cover = res.data.url
    },
    // 上传之前调用的方法
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg' // 限制文件类型
      const isLt2M = file.size / 1024 / 1024 < 2 // 限制文件大小

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    // 点击某个一级分类，就会触发@change，显示二级分类
    subjectLevelOneChanged(value) { // value就是一级分类的id值
      // 遍历所有的分类，包含一级和二级
      for (var i = 0; i < this.subjectOneList.length; i++) {
        // 得到每个一级分类
        var oneSubject = this.subjectOneList[i]
        // 判断：所有一级分类的id和点击的一级分类id是否一样
        if (value === oneSubject.id) {
          // 从一级分类里获取所有二级分类
          this.subjectTwoList = oneSubject.children
          // 把二级分类id值清空
          this.courseInfo.subjectId = ''
        }
      }
    },
    // 查询所有的一级分类
    getOneSubject() {
      subject.getSubjectList()
        .then(response => {
          this.subjectOneList = response.data.list
        })
    },
    // 查询所有讲师
    getListTeacher() {
      course.getTeacher()
        .then(response => {
          this.teacherList = response.data.items
        })
    },
    // 添加课程
    addCourse() {
      course.addCourseInfo(this.courseInfo)
        .then(response => {
          // 提示信息
          this.$message({
            type: 'success',
            message: '添加课程信息成功！'
          })

          // 跳转到第二步
          this.$router.push({ path: '/course/chapter/' + response.data.courseId })
        })
    },
    // 修改课程
    updateCourse() {
      course.updateCourseInfo(this.courseInfo)
        .then(response => {
          // 提示信息
          this.$message({
            type: 'success',
            message: '修改课程信息成功！'
          })
          this.$router.push({ path: '/course/chapter/' + this.courseId })
        })
    },
    saveOrUpdate() {
      // 判断是添加还是修改
      if (!this.courseInfo.id) {
        this.addCourse()
      } else {
        this.updateCourse()
      }
    }
  }
}

</script>

<style scoped>
.tinymce-container {
  line-height: 29px;
}
</style>
