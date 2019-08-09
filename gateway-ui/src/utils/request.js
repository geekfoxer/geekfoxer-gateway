import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// axios.defaults.withCredentials = true

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  },
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['X-Token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const data = response.data
    const retcode = parseInt(data.ret_code)
    // data.retcode = retcode
    console.log('返回的 retcode 是:', retcode)
    switch (retcode) {
      case 0:
        break
      case 30281009:
        // Toast(`${data.retmsg}`);
        // alert('无权限访问');
        // setTimeout(() => {
        // location.href = `https://passport.oa.fenqile.com/?url=https://fintech.lexin.com`;
        location.href = `https://www.fenqile.com/`
        // }, 1500);
        break
      case 19002028: // 跨域请求未登录错误码
        // Toast(`${data.retmsg}`);
        // setTimeout(() => {
        location.href = `https://passport.oa.fenqile.com/?url=https://fintech.lexin.com`
        // }, 1500);
        break
      default:
      // Toast(`${data.retmsg}`);
    }
    return data
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 2 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
