<template>
  <div class="monitor-test-container">
    <!-- 表单筛选条件 -->
    <el-card class="filter-container" shadow="never" style="margin-top: 10px;margin-bottom: 10px">
      <el-form :inline="true" :model="listQuery" label-width="140px">
        <el-row>
          <el-col :span="4">
            <span class="step-txt">第一步: 选择报警时间</span>
          </el-col>
          <el-col :span="6">
            <el-form-item label="">
              <el-date-picker
                v-model="warningTime"
                value-format="timestamp"
                align="right"
                type="date"
                placeholder="选择日期"
                :picker-options="pickerOptions" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-button type="info" icon="el-icon-search" @click="handleWarningDayTime">选择时间</el-button>
            <span class="note-txt">说明: 这里选择时间后,会出现当天的报表数据,尽量选择有异常(报警)时间</span>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="4">
            <span class="step-txt">第二步: 选择历史数据的跨度,测试波动值：</span>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-radio-group v-model="historyTimeDuration">
                <el-radio :label="7" border>一周</el-radio>
                <el-radio :label="14" border>二周</el-radio>
                <el-radio :label="21" border>三周</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-button type="primary" icon="el-icon-search" @click="handleVolatilityTest">波动幅度</el-button>
            <span class="note-txt">说明: 这里进行后,会绘画出预测值以及最后的波动幅度值的图像</span>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="4">
            <span class="step-txt">第三步: 填写报警阈值：</span>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-input-number v-model="thresholdValue" :precision="2" :step="0.01" :max="10" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-button type="danger" icon="el-icon-search" @click="handleSaveThreshold">提交阈值</el-button>
            <span class="note-txt">说明: 经过前面的两步后, 这里对第二步的图像进行阈值判断,填入此处,进行提交</span>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <div>
      <el-row>
        <el-col :span="18">
          <v-chart :options="chartNewData" />
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18">
          <v-chart :options="predictValueData" />
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18">
          <v-chart :options="volatilityValueData" />
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script>
import ECharts from 'vue-echarts'
import 'echarts/lib/chart/line'
import 'echarts/lib/component/tooltip'
import 'echarts/lib/component/legend'
import 'echarts/lib/component/title'
import 'echarts/lib/chart/bar'
import { getMonitorIdlData, getMonitorChartData, handleVolatilityData } from '@/api/monitor'
import { jsonToHump } from '@/utils/index'
import dateTimePickerOptions from './data'
const defaultListQuery = {
  pageNum: 1,
  pageSize: 10,
  name: '',
  title: ''
}

const yesterday = new Date()
yesterday.setTime(yesterday.getTime() - 3600 * 1000 * 24)

export default {
  name: 'Index',
  components: {
    'v-chart': ECharts
  },
  data() {
    return {
      pickerOptions: dateTimePickerOptions(),
      warningTime: yesterday,
      historyTimeDuration: 14,
      thresholdValue: 0.00,
      listQuery: Object.assign({}, defaultListQuery),
      chartNewData: {},
      predictValueData: {},
      volatilityValueData: {}
    }
  },
  created() {
    console.log('created().....')
    getMonitorIdlData().then(res => {
      console.log('获取到 idl 的数据', res)
    })
  },
  methods: {
    handleWarningDayTime() {
      console.log('测试报警天......', this.warningTime)
      getMonitorChartData(this.warningTime).then(res => {
        const chartOption = res.result_rows[0].data
        jsonToHump(chartOption)
        console.log('图标的数据是:', chartOption)
        this.chartNewData = Object.assign({}, chartOption)
      })
    },
    handleVolatilityTest() {
      console.log('测试波动浮动值', this.historyTimeDuration)
      handleVolatilityData(this.warningTime, this.historyTimeDuration).then(res => {
        const volatilityValue = res.result_rows[0].volatility_value
        const predictValue = res.result_rows[0].predict_value
        jsonToHump(volatilityValue)
        jsonToHump(predictValue)
        this.predictValueData = Object.assign({}, predictValue)
        this.volatilityValueData = Object.assign({}, volatilityValue)
      })
    },
    handleSaveThreshold() {
      console.log('提交的阈值', this.thresholdValue)
    }
  }
}
</script>

<style scoped>
  .step-txt {
    color: #606266;
    font-size: 14px;
    font-weight: 700;
  }
  .note-txt {
    font-size: 14px;
    color: #909399;
  }
</style>
