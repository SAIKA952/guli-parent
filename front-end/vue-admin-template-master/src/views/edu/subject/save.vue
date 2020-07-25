<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="信息描述">
        <el-tag type="info">excel模版说明</el-tag>
        <el-tag>
          <i class="el-icon-download"/>
          <a :href="OSS_PATH + '/excel/01.xlsx'">点击下载模版</a>
        </el-tag>

      </el-form-item>

      <!--  ref：唯一标识，相当于html中的id
            auto-upload：是否需要自动上传，就是选择了文件后是否自动帮你上传
            on-success/on-error：上传成功/失败调用等号后面的方法
            disabled：按钮点完之后不能在被点第二次
            limit：限制每次只能上传一个文件
            action：后端接口的地址
            name：相当于type，这里name="file"就相当于input中的type="file"
            accept：设置上传文件的格式，这里ms-excel设置了只能上传微软的excel文件 -->
      <el-form-item label="选择Excel">
        <el-upload
          ref="upload"
          :auto-upload="false"
          :on-success="fileUploadSuccess"
          :on-error="fileUploadError"
          :disabled="importBtnDisabled"
          :limit="1"
          :action="BASE_API+'/eduservice/edu-subject/addSubject'"
          name="file"
          accept="application/vnd.ms-excel">
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button
            :loading="loading"
            style="margin-left: 10px;"
            size="small"
            type="success"
            @click="submitUpload">上传到服务器</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      BASE_API: process.env.BASE_API, // 接口API地址
      OSS_PATH: 'https://edu-952.oss-cn-hangzhou.aliyuncs.com', // 阿里云OSS地址
      importBtnDisabled: false, // 按钮是否禁用,
      loading: false
    }
  },
  created() {

  },
  methods: {
    // 点击按钮上传文件到接口里的方法
    submitUpload() {
      this.importBtnDisabled = true
      this.loading = true
      // 相当于js中document.getElementById("upload").submit()，upload就是ref中的upload
      this.$refs.upload.submit()
    },
    fileUploadSuccess() { // 上传成功的方法
      // 提示信息
      this.loading = false
      this.$message({
        type: 'success',
        message: '添加课程分类成功'
      })
      // 跳转课程分类列表
      this.$router.push({ path: '/subject/list' })
    },
    fileUploadError() { // 上传失败的方法
      this.loading = false
      this.$message({
        type: 'success',
        message: '添加课程分类失败'
      })
    }
  }
}
</script>
