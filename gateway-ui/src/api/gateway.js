import request from '@/utils/request'

export function helloTest() {
  return request({
    url: '/test',
    method: 'GET'
  })
}

export function getApiList(params) {
  return request({
    url: '/gateway/api/list',
    method: 'GET',
    params: params
  })
}

export function saveApiInfo(params) {
  return request({
    url: '/gateway/api/save',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: params
  })
}

export function testApi(params) {
  return request({
    url: '/gateway/api/test',
    method: 'POST',
    params: params
  })
}

export function deleteApiInfo(id) {
  return request({
    url: '',
    method: 'POST',
    params: { 'id': id }
  })
}
