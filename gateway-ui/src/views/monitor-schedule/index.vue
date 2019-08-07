<template>
  <div class="monitor-schedule-container">
    <!-- 显示的表格 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      style="width: 100%">

      <el-table-column prop="monitorId" label="监控ID" align="center" />
      <el-table-column prop="monitorName" label="监控名称" align="center" />

      <el-table-column label="操作" align="center" width="330" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleUpdateDietitian(scope.row)">开启</el-button>
          <el-button v-if="scope.row.status!=='deleted'" type="danger" @click="handleDelete(scope.row)">暂停</el-button>
          <el-button type="primary" @click="handleUpdateGoodsDetail(scope.row)">编辑详情</el-button>
          <el-button type="success" @click="handleUpdateGoods(scope.row)">编辑单品</el-button>
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
const defaultListQuery = {
  pageNum: 1,
  pageSize: 10,
  name: '',
  title: ''
}
export default {
  name: 'Index',
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: Object.assign({}, defaultListQuery)
    }
  },
  created() {
    console.log('created().....')
  },
  methods: {}
}
</script>

<style scoped>

</style>
