<template>

  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="最终发布"/>
    </el-steps>

    <el-button type="text" @click="openChapterDialog()">添加章节</el-button>

    <!-- 章节 -->
    <ul class="chanpterList">
      <li
        v-for="chapter in chapterVideoList"
        :key="chapter.id">
        <p>
          {{ chapter.title }}

          <span class="acts">
            <el-button style="" type="text" @click="openVideo(chapter.id)">添加小节</el-button>
            <el-button style="" type="text" @click="openEditChapter(chapter.id)">编辑</el-button>
            <el-button type="text" @click="removeChapter(chapter.id)">删除</el-button>
          </span>
        </p>

        <!-- 视频 -->
        <ul class="chanpterList videoList">
          <li
            v-for="video in chapter.children"
            :key="video.id">
            <p>{{ video.title }}
              <span class="acts">
                <el-button style="" type="text" @click="editVideo(video.id)">编辑</el-button>
                <el-button type="text" @click="removeVideo(video.id)">删除</el-button>
              </span>
            </p>
          </li>
        </ul>
      </li>
    </ul>
    <div>
      <el-button @click="previous">上一步</el-button>
      <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
    </div>

    <!-- 添加和修改章节表单 -->
    <el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
      <el-form :model="chapter" label-width="120px">
        <el-form-item label="章节标题">
          <el-input v-model="chapter.title"/>
        </el-form-item>
        <el-form-item label="章节排序">
          <el-input-number v-model="chapter.sort" :min="0" controls-position="right"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加和修改课时表单 -->
    <el-dialog :visible.sync="dialogVideoFormVisible" title="添加课时">
      <el-form :model="video" label-width="120px">
        <el-form-item label="课时标题">
          <el-input v-model="video.title"/>
        </el-form-item>
        <el-form-item label="课时排序">
          <el-input-number v-model="video.sort" :min="0" controls-position="right"/>
        </el-form-item>
        <el-form-item label="是否免费">
          <el-radio-group v-model="video.free">
            <el-radio :label="true">免费</el-radio>
            <el-radio :label="false">默认</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="上传视频">
          <!-- on-success:上传成功执行的方法
                 on-remove：删除时调用的方法
                 before-remove：删除之前调用的方法，一般是弹框确认
                 on-exceed：上传文件之前的设置
                 file-list：显示文件列表的方法
                 action：后端接口地址
                 limit：允许上传文件的数量
             -->
          <el-upload
            :on-success="handleVodUploadSuccess"
            :on-remove="handleVodRemove"
            :before-remove="beforeVodRemove"
            :on-exceed="handleUploadExceed"
            :file-list="fileList"
            :action="BASE_API+'/eduvod/video/uploadAliyunVideo'"
            :limit="1"
            class="upload-demo">
            <el-button size="small" type="primary">上传视频</el-button>
            <el-tooltip placement="right-end">
              <div slot="content">最大支持1G，<br>
                支持3GP、ASF、AVI、DAT、DV、FLV、F4V、<br>
                GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、<br>
                MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、<br>
                SWF、TS、VOB、WMV、WEBM 等视频格式上传</div>
              <i class="el-icon-question"/>
            </el-tooltip>
          </el-upload>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVideoFormVisible = false">取 消</el-button>
        <el-button :disabled="saveVideoBtnDisabled" type="primary" @click="saveOrUpdateVideo">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import chapter from '@/api/edu/chapter'
import video from '@/api/edu/video'

export default {
  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用
      saveVideoBtnDisabled: false,
      chapterVideoList: [],
      courseId: '', // 课程id
      dialogChapterFormVisible: false, // 章节弹框
      dialogVideoFormVisible: false, // 小节弹框
      fileList: [], // 上传文件列表
      BASE_API: process.env.BASE_API, // 接口API地址
      chapter: { // 封装章节数据
        title: '',
        sort: 0
      },
      video: {
        title: '',
        sort: 0,
        free: 0,
        videoSourceId: '',
        videoOriginalName: '' // 视频名称
      }
    }
  },

  created() {
    // 获取路由中的id值
    if (this.$route.params && this.$route.params.id) {
      this.courseId = this.$route.params.id
      this.getChapterVideo()
    }
  },
  methods: {
    beforeVodRemove(file, fileList) { // 点击x调用
      return this.$confirm(`确定移除 ${file.name}?`)
    },
    handleVodRemove() { // 点击确认调用
      // 调用接口中删除视频的方法
      video.deleteAliyunVod(this.video.videoSourceId)
        .then(response => {
          this.$message({ // 提示信息
            type: 'success',
            message: '删除视频成功!'
          })
          // 把文件列表清空
          this.fileList = []
          // 把video视频id和视频名称清空
          this.video.videoSourceId = ''
          this.video.videoOriginalName = ''
        })
    },
    // 上传视频成功调用的方法
    handleVodUploadSuccess(response, file, fileList) {
      this.video.videoSourceId = response.data.videoId // 得到视频id
      this.video.videoOriginalName = file.name // 得到视频名称
    },
    // 上传之前的方法
    handleUploadExceed() {
      this.$message.warning('想要重新上传视频，请先删除已上传的视频')
    },
    // ===================================小节部分============================
    // 删除小节
    removeVideo(id) {
      this.$confirm('此操作将永久删除小节，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用删除方法
        video.deleteVideo(id)
          .then(response => {
            this.$message({ // 提示信息
              type: 'success',
              message: '删除成功!'
            })
            this.getChapterVideo() // 刷新页面
          })
      })
    },
    editVideo(videoId) { // 编辑小节
      // 弹框
      this.dialogVideoFormVisible = true
      // 调用接口
      video.getVideo(videoId)
        .then(Response => {
          this.video.title = Response.data.video.title
          this.video.sort = Response.data.video.sort
          console.log(Response.data.video.isFree)
          this.video.videoSourceId = Response.data.video.videoSourceId
          // this.video = Response.data.videoId
        })
    },
    openVideo(chapterId) { // 添加小节弹框的方法
      this.video = {} // 再次点击添加小节，表中数据清空
      this.dialogVideoFormVisible = true

      // 设置章节id
      this.video.chapterId = chapterId
    },
    addVideo() { // 添加小节
      // 设置课程id
      this.video.courseId = this.courseId
      video.addVideo(this.video)
        .then(response => {
          // 关闭弹框
          this.dialogVideoFormVisible = false

          // 提示信息
          this.$message({
            type: 'success',
            message: '添加小节成功!'
          })
          // 刷新页面
          this.getChapterVideo() // 再查一次章节和小节
        })
    },
    saveOrUpdateVideo() {
      this.addVideo()
    },

    // ===================================章节部分============================
    // 删除章节
    removeChapter(chapterId) {
      this.$confirm('此操作将永久删除章节，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用删除方法
        chapter.deleteChapter(chapterId)
          .then(response => {
            this.$message({ // 提示信息
              type: 'success',
              message: '删除成功!'
            })
            this.getChapterVideo() // 刷新页面
          })
      })
    },
    // 修改章节弹框数据回显
    openEditChapter(chapterId) {
      // 弹框
      this.dialogChapterFormVisible = true
      // 调用接口
      chapter.getChapter(chapterId)
        .then(Response => {
          this.chapter = Response.data.chapter
        })
    },
    openChapterDialog() { // 弹出添加章节页面
      // 弹框
      this.dialogChapterFormVisible = true
      // 表单数据清空
      this.chapter.title = ''
      this.chapter.sort = 0
    },
    addChapter() {
      // 设置课程id到chapter对象中去
      this.chapter.courseId = this.courseId
      chapter.addChapter(this.chapter)
        .then(Response => {
          // 关闭弹框
          this.dialogChapterFormVisible = false

          // 提示信息
          this.$message({
            type: 'success',
            message: '添加章节成功!'
          })
          // 刷新页面
          this.getChapterVideo() // 再查一次章节和小节
        })
    },
    saveOrUpdate() {
      if (!this.chapter.id) { // 如果没有id，就执行添加操作
        this.addChapter()
      } else {
        this.updateChapter(chapter)
      }
    },
    // 修改章节的方法
    updateChapter(chapter) {
      chapter.updateChapter(this.chapter)
        .then(Response => {
        // 关闭弹框
          this.dialogChapterFormVisible = false
          // 提示信息
          this.$message({
            type: 'success',
            message: '修改章节成功!'
          })
          // 刷新页面
          this.getChapterVideo() // 再查一次章节和小节
        })
    },
    // 根据课程id查询章节和小节
    getChapterVideo() {
      chapter.getAllChapterVideo(this.courseId)
        .then(Response => {
          this.chapterVideoList = Response.data.allChapterVideo
        })
    },
    previous() { // 上一步的方法
      this.$router.push({ path: '/course/info/' + this.courseId })
    },

    next() { // 下一步的方法
      this.$router.push({ path: '/course/publish/' + this.courseId })
    }
  }
}
</script>

<style scoped>
.chanpterList{
    position: relative;
    list-style: none;
    margin: 0;
    padding: 0;
}
.chanpterList li{
  position: relative;
}
.chanpterList p{
  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  width: 100%;
  border: 1px solid #DDD;
}
.chanpterList .acts {
    float: right;
    font-size: 14px;
}

.videoList{
  padding-left: 50px;
}
.videoList p{
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;
  line-height: 30px;
  width: 100%;
  border: 1px dotted #DDD;
}

</style>
