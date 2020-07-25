<template>
  <div class="app-container">
    讲师添加
    <el-form label-width="120px">
      <el-form-item label="讲师名称">
        <el-input v-model="teacher.name"/>
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number v-model="teacher.sort" controls-position="right" /> <!--min="0"-->
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level" clearable placeholder="请选择">
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
          -->
          <el-option :value="1" label="高级讲师"/>
          <el-option :value="2" label="首席讲师"/>
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-input v-model="teacher.career"/>
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-input v-model="teacher.intro" :rows="10" type="textarea"/>
      </el-form-item>

      <!-- 讲师头像：TODO -->
      <!-- 讲师头像 -->
      <el-form-item label="讲师头像">

        <!-- 头衔缩略图 -->
        <pan-thumb :image="teacher.avatar"/>
        <!-- 文件上传按钮 -->
        <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">更换头像
        </el-button>

        <!--
        v-show：是否显示上传组件
        :key：类似于id，如果一个页面多个图片上传控件，可以做区分
        :url：后台上传的url地址
        @close：关闭上传组件
        @crop-upload-success：上传成功后的回调 -->
        <image-cropper
          v-show="imagecropperShow"
          :width="300"
          :height="300"
          :key="imagecropperKey"
          :url="BASE_API+'/eduoss/fileoss'"
          field="file"
          @close="close"
          @crop-upload-success="cropSuccess"/>

      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import teacherApi from '@/api/edu/teacher'
// 引入组件
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

export default {
  // 声明组件
  components: { ImageCropper, PanThumb },
  data() {
    return {
      teacher: {
        name: '',
        sort: 0,
        level: 1,
        career: '',
        intro: '',
        avatar: ''
      },
      // 上传弹框组件是否显示
      imagecropperShow: false,
      // 上传组件key值
      imagecropperKey: 0,
      // 获取dev.env.js里面的地址
      BASE_API: process.env.BASE_API,
      // 保存按钮是否禁用
      saveBtnDisabled: false // 保存按钮是否禁用
    }
  },
  watch: { // 监听
    $route(to, from) { // 路由变化方式，路由发生变化，方法就会执行
      this.init()
    }
  },
  created() { // 页面渲染之前执行
    this.init()
  },
  methods: {
    close() { // 关闭上传弹框的方法
      this.imagecropperShow = false
      // 上传组件初始化
      this.imagecropperKey++
    },
    cropSuccess(data) { // 上传成功方法
      this.imagecropperShow = false
      // 上传之后接口返回图片的地址
      this.teacher.avatar = data.url
      this.imagecropperKey++
    },
    init() {
      // 判断路径是否有id值，有就做修改
      if (this.$route.params && this.$route.params.id) {
        // 从路径中获取值
        const id = this.$route.params.id
        // 调用根据id查询的方法
        this.getTeacherInfo(id)
      } else {
        // 没有id值，做添加
        // 清空表单
        this.teacher = {}
      }
    },
    // 根据讲师id查询方法
    getTeacherInfo(id) {
      teacherApi.getTeacherById(id)
        .then(Response => {
          this.teacher = Response.data.teacher
        })
    },
    updateTeacherInfo() { // 修改讲师的方法
      teacherApi.updateTeacher(this.teacher)
        .then(response => {
          this.$message({ // 提示信息
            type: 'success',
            message: '修改成功!'
          })
          // 回到列表页面，要进行路由跳转
          this.$router.push({ path: '/teacher/table' })
        })
    },
    saveOrUpdate() { // 写成saveOrUpdate是因为等等做修改操作的时候可以复用这个方法
      // 判断是修改还是添加
      // 根据teacher里是否有id进行判断，如果有id就是修改，没id就是添加
      if (!this.teacher.id) {
        // 添加
        this.saveTeacher()
      } else {
        // 修改
        this.updateTeacherInfo()
      }
    },
    saveTeacher() {
      teacherApi.addTeacher(this.teacher)
        .then(Response => { // 添加成功
          this.$message({ // 提示信息
            type: 'success',
            message: '添加成功!'
          })
          // 回到列表页面，要进行路由跳转
          // 因为save方法是在/teacher/save下，而讲师列表是在/teacher/table下
          this.$router.push({ path: '/teacher/table' })
        })
    }
  }
}
</script>
