<template>
  <div class="dashboard-container">
    <el-row>
      <el-col :span="12">
        <v-chart autoresize :options="option" />
      </el-col>
      <el-col :span="12">
        <v-chart autoresize :options="option" />
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col />
    </el-row>

  </div>
</template>

<script>
import ECharts from 'vue-echarts'
import 'echarts/lib/chart/line'
import 'echarts/lib/component/tooltip'
import 'echarts/lib/component/legend'
import 'echarts/lib/component/title'
import 'echarts/lib/chart/bar'

// import 'echarts/lib/component/polar'
import { getMonitorIdlData, getMonitorChartData } from '@/api/monitor'
import { helloTest } from '@/api/gateway'
import { jsonToHump } from '@/utils/index'
import getBar from './bar'

var now = +new Date(1997, 9, 3)
var oneDay = 24 * 3600 * 1000
var value = Math.random() * 1000

export default {
  name: 'Dashboard',
  components: {
    'v-chart': ECharts
  },
  data() {
    return {
      bar: getBar(),
      timeLineData: [],
      chartNewData: {},
      option: {
        title: { text: '动态数据111 + 时间坐标轴' },
        grid: {
          bottom: '30px',
          left: '50px',
          right: '2.5%'
        },
        tooltip: {
          trigger: 'axis'
          // formatter: function(params) {
          //   params = params[0]
          //   var date = new Date(params.name)
          //   return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' : ' + params.value[1]
          // },
          // axisPointer: {
          //   animation: false
          // }
        },
        // legend: {
        //   orient: 'vertical',
        //   left: 'left',
        //   data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
        // },
        legend: {
          selected: { 'PV': true, 'UV': false },
          data: ['PV', 'UV'],
          textStyle: { color: '#e3e3e3' }
        },
        xAxis: {
          type: 'category',
          boundaryGap: 0,
          splitLine: { show: true },
          data: ['12:00', '12:05', '12:10', '12:15', '12:20', '12:25', '12:30', '12:35', '12:40', '12:45', '12:50', '12:55', '13:00', '13:05', '13:10', '13:15', '13:20', '13:25', '13:30', '13:35', '13:40', '13:45', '13:50', '13:55']
        },
        yAxis: {
          type: 'value',
          name: '',
          splitLine: { show: true }
        },
        series: [{
          name: 'PV',
          type: 'line',
          showSymbol: true,
          showAllSymbol: true,
          smooth: true,
          yAxisIndex: 0,
          itemStyle: {
            normal: {
              lineStyle: {
                width: 1.3
              }
            }
          },
          data: ['177.0', '151.0', '162.0', '153.0', '166.0', '168.0', '177.0', '186.0', '178.0', '200.0', '173.0', '198.0', '182.0', '174.0', '182.0', '171.0', '183.0', '195.0', '165.0', '178.0', '188.0', '169.0', '172.0', '201.0']
        }, {
          name: 'UV',
          type: 'line',
          showSymbol: false,
          showAllSymbol: false,
          yAxisIndex: 0,
          data: ['134.0', '145.0', '123.0', '100.0', '166.0', '168.0', '177.0', '186.0', '178.0', '200.0', '173.0', '198.0', '182.0', '174.0', '182.0', '171.0', '183.0', '195.0', '165.0', '178.0', '188.0', '169.0', '172.0', '201.0']
        }]
      }
    }
  },
  created() {
    this.getMonitorIdlData()
    getMonitorChartData().then(res => {
      const chartOption = res.result_rows[0].data
      jsonToHump(chartOption)
      console.log('图标的数据是:', chartOption)
      this.chartNewData = Object.assign({}, chartOption)
    })
    helloTest().then(res => {
      console.log('获取的后台数据是:', res)
    })
  },
  methods: {
    getMonitorIdlData() {
      getMonitorIdlData().then(res => {
        console.log(res.result_rows)
      })
    },
    randomData() {
      now = new Date(+now + oneDay)
      value = value + Math.random() * 21 - 10
      return {
        name: now.toString(),
        value: [
          [now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'),
          Math.round(value)
        ]
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
