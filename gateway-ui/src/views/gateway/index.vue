<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">添加
      </el-button>
    </div>
    <!--    添加接口的 dialog-->
    <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      width="40%">
      <el-form
        ref="dataForm"
        :model="temp"
        label-position="right"
        label-width="110px"
        style="margin-left:20px;margin-right: 20px"
        :rules="rules">
        <el-form-item label="API名称：" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>

        <el-form-item label="请求路径：" prop="url">
          <el-input v-model="temp.url" />
        </el-form-item>

        <el-form-item label="请求方法：">
          <el-radio-group v-model="temp.httpMethod">
            <el-radio label="GET" border>GET</el-radio>
            <el-radio label="POST" border>POST</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="分组：">
          <el-select v-model="temp.groupId" placeholder="请选择">
            <el-option value="1" label="服务化">服务化</el-option>
            <el-option value="2" label="聚合支付">聚合支付</el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="服务名：" prop="serviceName">
          <el-input v-model="temp.serviceName" />
        </el-form-item>

        <el-form-item label="方法名：" prop="methodName">
          <el-input v-model="temp.methodName" />
        </el-form-item>

        <el-form-item label="参数模板：" prop="methodName">
          <el-input v-model="temp.dubboParamTemplate" />
        </el-form-item>

        <el-form-item label="API描述：">
          <el-input
            v-model="temp.describe"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="Please input" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">确认</el-button>
      </div>
    </el-dialog>

    <!--    测试接口数据的 dialog-->
    <el-dialog
      title="测试接口"
      :visible.sync="dialogTestApiFormVisible"
      :close-on-click-modal="false"
      width="40%">
      <el-form
        ref="dataForm"
        :model="apiTest"
        label-position="right"
        label-width="140px"
        style="margin-left:20px;margin-right: 20px"
        :rules="rules">

        <el-form-item label="参数(json格式)：">
          <el-input
            v-model="apiTest.jsonBody"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="Please input" />
        </el-form-item>
        <el-form-item label="请求结果:">
          <el-card class="box-card">
            <div v-text="apiTestResult" />
          </el-card>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleTestApi()">测试接口</el-button>
      </div>
    </el-dialog>

    <!-- 显示的表格 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      style="width: 100%">
      <el-table-column prop="id" label="ID" align="center" width="60" />
      <el-table-column prop="name" label="名称" align="center" :show-overflow-tooltip="true" />
      <el-table-column prop="describe" label="描述" align="center" :show-overflow-tooltip="true" />
      <el-table-column prop="url" label="url" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.url | formatGatewayUrl }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="httpMethod" label="方法" align="center" width="80" />
      <el-table-column prop="serviceName" label="请求服务" align="center" />
      <el-table-column prop="methodName" label="请求方法" align="center" />
      <el-table-column prop="dubboParamTemplate" label="模板参数" align="center" />

      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" @click="tapTestApi(scope.row)">测试接口</el-button>
          <el-button type="danger" @click="tapDeleteApi(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <v-pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.pageNum"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { getApiList, saveApiInfo, testApi, deleteApiInfo } from '@/api/gateway'
import { getFormRules } from './data'
const defaultListQuery = {
  pageNum: 1,
  pageSize: 10
}
export default {
  name: 'Index',
  data() {
    return {
      rules: getFormRules(),
      dialogFormVisible: false,
      dialogTestApiFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑信息',
        create: '添加信息'
      },
      temp: {},
      apiTest: {},
      apiTestResult: '',
      list: [],
      total: 0,
      listLoading: true,
      listQuery: Object.assign({}, defaultListQuery)
    }
  },
  created() {
    console.log('created().....')
    this.getList()
  },
  methods: {
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        imageUrl: ''
      }
    },
    getList() {
      this.listLoading = true
      getApiList(this.listQuery).then(res => {
        console.log(res)
        this.list = res.data
        this.total = res.total
        console.log('this.list', this.list)
        console.log('this.total', this.total)
        setTimeout(() => {
          this.listLoading = false
        }, 0.2 * 1000)
      })
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      console.log('create data...')
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const data = Object.assign({}, this.temp)
          data.routeType = 1
          console.log('最后保存的数据是:', data)
          saveApiInfo(data).then(res => {
            this.dialogFormVisible = false
            this.getList()
            this.resetTemp()
            this.$notify({ title: '成功', message: '添加成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    updateData() {
      console.log('update data....')
    },
    tapTestApi(row) {
      this.apiTestResult = ''
      this.apiTest = {}
      this.dialogTestApiFormVisible = true
      this.apiTest.uri = row.url
    },
    handleTestApi() {
      const data = Object.assign({}, this.apiTest)
      console.log('请求参数', data)
      testApi(data).then(res => {
        this.apiTestResult = res.result_rows[0]
        console.log('测试接口获取的信息', res)
      })
    },
    tapDeleteApi(row) {
      this.$confirm('此操作将删除条目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        closeOnClickModal: false,
        type: 'warning'
      }).then(() => {
        console.log('执行删除操作')
        deleteApiInfo(row.id).then((response) => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        })
      }).catch(() => {
        this.$notify({
          message: '已取消删除',
          type: 'info'
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
