<template>
  <div class="app-container">
    讲师列表

    <!--查询表单 inline表示在一行显示-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <!-- v-model双向绑定，
          这边teacherQuery中并没有name属性，但是js会帮我们自动创建 -->
        <el-input v-model="teacherQuery.name" placeholder="讲师名"/>
      </el-form-item>

      <el-form-item>
        <el-select v-model="teacherQuery.level" clearable placeholder="讲师头衔">
          <el-option :value="1" label="高级讲师"/>
          <el-option :value="2" label="首席讲师"/>
        </el-select>
      </el-form-item>

      <el-form-item label="添加时间">
        <el-date-picker
          v-model="teacherQuery.begin"
          type="datetime"
          placeholder="选择开始时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>
      <el-form-item>
        <!-- 时间选择框 -->
        <el-date-picker
          v-model="teacherQuery.end"
          type="datetime"
          placeholder="选择截止时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
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

      <el-table-column prop="name" label="名称" width="80" />

      <el-table-column label="头衔" width="80">
        <template slot-scope="scope">
          <!-- ===表示判断值和类型，==只判断值 -->
          {{ scope.row.level===1?'高级讲师':'首席讲师' }}
        </template>
      </el-table-column>

      <el-table-column prop="intro" label="资历" />

      <el-table-column prop="gmtCreate" label="添加时间" width="160"/>

      <el-table-column prop="sort" label="排序" width="60" />

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <router-link :to="'/teacher/edit/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit">修改</el-button>
          </router-link>
          <!-- scope表示所有表格，scope.row表示得到表格每一行，scope.row.id表示得到行中的id值 -->
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <!-- :相当于v-bind，@实际上就是v-on: -->
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
import teacher from '@/api/edu/teacher'

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
      teacherQuery: {} // 条件封装对象
    }
  },
  created() { // 页面渲染之前执行，调用methods定义的方法
    // 调用
    this.getList()
  },
  methods: { // 创建具体的方法，调用teacher.js定义的方法
    // 讲师列表的方法
    getList(page = 1) { // 参数这里传一个page，默认为1
      this.page = page // 每次传的page都不一样，如果不传就默认是1，传了就把值赋给this.page
      teacher.getTeacherListPage(this.page, this.limit, this.teacherQuery)
        .then(response => { // 请求成功
          // response接口返回的数据
          console.log(response)
          this.list = response.data.rows // 获取所有教师数据
          this.total = response.data.total // 获取总记录数
          console.log(this.list)
          console.log(this.total)
        })
        .catch(error => { // 请求失败
          console.log(error)
        })
    },
    resetData() { // 清空的方法
      // 1、表单输入项数据清空
      this.teacherQuery = {}
      // 2、查询所有讲师数据
      this.getList()
    },
    removeDataById(id) { // 删除讲师的方法
      this.$confirm('此操作将永久删除该, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用删除方法
        teacher.deleteTeacherById(id).then(response => {
          this.$message({ // 提示信息
            type: 'success',
            message: '删除成功!'
          })
          this.getList() // 刷新页面
        })
      }).catch(() => { })
    }
  }
}
</script>
