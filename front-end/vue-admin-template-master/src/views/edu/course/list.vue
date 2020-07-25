<template>
  <div class="app-container">
    课程列表

    <!--查询表单 inline表示在一行显示-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="courseQuery.title" placeholder="课程名称"/>
      </el-form-item>

      <el-form-item>
        <el-select v-model="courseQuery.status" clearable placeholder="课程状态">
          <el-option :value="'Normal'" label="已发布"/>
          <el-option :value="'Draft'" label="未发布"/>
        </el-select>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <!-- 表格 -->
    <!-- :data表示从data()中取值，将list下的所有结果遍历出来，:相当于v-bind -->
    <el-table
      :data="list"
      border
      fit
      highlight-current-row>

      <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="title" label="课程名称" width="160" />

      <el-table-column label="课程状态" width="80">
        <template slot-scope="scope">
          <!-- ===表示判断值和类型，==只判断值 -->
          {{ scope.row.status==='Normal'?'已发布':'未发布' }}
        </template>
      </el-table-column>

      <el-table-column prop="lessonNum" label="课时数" width="60"/>

      <el-table-column prop="gmtCreate" label="添加时间" width="160"/>

      <el-table-column prop="viewCount" label="浏览数量" width="60" />

      <el-table-column label="操作" width="500" align="center">
        <template slot-scope="scope">
          <router-link :to="'/teacher/edit/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit">编辑课程基本信息</el-button>
          </router-link>
          <router-link :to="'/teacher/edit/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit">编辑课程大纲</el-button>
          </router-link>
          <!-- scope表示所有表格，scope.row表示得到表格每一行，scope.row.id表示得到行中的id值 -->
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="page"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="getList"
    />

    <!-- page：当前页，limit：每页记录数，total：总记录数，getList：分页的方法 -->
  </div>
</template>

<script>
// 引入调用teacher.js文件
import course from '@/api/edu/course'

export default {
  // 写核心代码位置
  // data:{

  // },
  // 建议这样写
  data() { // 定义变量和初始值
    return {
      list: null, // 查询之后接口返回集合
      page: 1, // 当前页
      limit: 10, // 每页记录数
      total: 0, // 总记录数
      courseQuery: {} // 条件封装对象
    }
  },
  created() { // 页面渲染之前执行，调用methods定义的方法
    // 调用
    this.getList()
  },
  methods: {
    getList(page = 1) { // 参数这里传一个page，默认为1
      this.page = page
      course.getListCourse(this.page, this.limit, this.courseQuery)
        .then(response => { // 请求成功
          this.total = response.data.total // 获取总记录数
          this.list = response.data.rows // 获取所有课程数据
        })
    },
    resetData() { // 清空的方法
      this.courseQuery = {}
      this.getList()
    }
  }
}
</script>
