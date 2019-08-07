import request from '@/utils/request'

export function getMonitorIdlData() {
  return request({
    url: '/query_monitoridl_data.json',
    method: 'POST'
  })
}

export function getMonitorChartData(queryTime) {
  return request({
    url: 'query_chart_data.json',
    method: 'POST',
    params: { 'queryTime': queryTime }
  })
}

export function handleVolatilityData(queryTime, historyDay) {
  return request({
    url: 'handle_volatility_data.json',
    method: 'POST',
    params: { 'queryTime': queryTime, 'historyDay': historyDay }
  })
}
