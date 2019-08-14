// set function parseTime,formatTime to filter
export { parseTime, formatTime } from '@/utils'

function pluralize(time, label) {
  if (time === 1) {
    return time + label
  }
  return time + label + 's'
}

export function timeAgo(time) {
  const between = Date.now() / 1000 - Number(time)
  if (between < 3600) {
    return pluralize(~~(between / 60), ' minute')
  } else if (between < 86400) {
    return pluralize(~~(between / 3600), ' hour')
  } else {
    return pluralize(~~(between / 86400), ' day')
  }
}

/* 数字 格式化*/
export function numberFormatter(num, digits) {
  const si = [
    { value: 1E18, symbol: 'E' },
    { value: 1E15, symbol: 'P' },
    { value: 1E12, symbol: 'T' },
    { value: 1E9, symbol: 'G' },
    { value: 1E6, symbol: 'M' },
    { value: 1E3, symbol: 'k' }
  ]
  for (let i = 0; i < si.length; i++) {
    if (num >= si[i].value) {
      return (num / si[i].value + 0.1).toFixed(digits).replace(/\.0+$|(\.[0-9]*[1-9])0+$/, '$1') + si[i].symbol
    }
  }
  return num.toString()
}

export function toThousandFilter(num) {
  return (+num || 0).toString().replace(/^-?\d+/g, m => m.replace(/(?=(?!\b)(\d{3})+$)/g, ','))
}

/** 活动状态判断显示 */
export function formatActiveStatus(row) {
  const nowTime = new Date().getTime()
  if (nowTime >= row.startTime && nowTime <= row.endTime) {
    // return '活动进行中'
    return '<span class="el-tag el-tag--success el-tag--small">活动进行中<span/>'
  } else if (nowTime > row.endTime) {
    // return '活动已结束'
    return '<span class="el-tag el-tag--info el-tag--small">活动已结束</span>'
  } else {
    // return '活动未开始'
    return '<span class="el-tag el-tag--small">活动未开始</span>'
  }
}

export function formatGoodsType(value) {
  if (value === 1) {
    return '单品'
  } else if (value === 2) {
    return '套餐'
  }
}

export function formatPayType(value) {
  if (value === 1) {
    return '微信'
  } else if (value === 2) {
    return '支付宝'
  } else if (value === 3) {
    return '余额'
  } else {
    return '未支付'
  }
}

export function formatDelete(value) {
  if (value === 0) {
    return '未删除'
  } else if (value === 1) {
    return '已删除'
  }
}

const defaultTypeMap = {
  1: { id: 1, name: '首页', pixel: '720 * 420' },
  2: { id: 2, name: '分类页', pixel: '502 * 166' },
  3: { id: 3, name: '健康定制首页', pixel: '672 * 320' },
  4: { id: 4, name: '健康定制下某一分类页', pixel: '672 * 200' },
  5: { id: 5, name: '团购', pixel: '672 * 320' },
  6: { id: 6, name: '预售和折扣', pixel: '672 *3 20' }
}

export function formatBannerType(val) {
  if (val === 1) {
    return '首页'
  } else if (val === 2) {
    return '分类页'
  } else if (val === 3) {
    return '健康定制首页'
  } else if (val === 4) {
    return '健康定制下某一分类页'
  } else if (val === 5) {
    return '团购'
  } else if (val === 6) {
    return '预售和折扣'
  } else {
    return '未知'
  }
}
export function formatBannerRecSize(val) {
  if (defaultTypeMap[val] !== undefined) {
    return defaultTypeMap[val].pixel
  } else {
    return '未知'
  }
}

export function formatGatewayUrl(val) {
  return val.replace('/oa/data/', '').replace('.json', '')
}
