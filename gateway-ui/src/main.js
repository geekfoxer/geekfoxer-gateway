import Vue from 'vue'
import App from './App'
import store from './store'
import router from './router'

// element-ui
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import '@/styles/index.scss' // global css

import '@/icons' // icon
import '@/permission' // permission control

// refers to components/ECharts.vue in webpack
import ECharts from 'vue-echarts'

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */
// import { mockXHR } from '../mock'
// if (process.env.NODE_ENV === 'production') {
//   mockXHR()
// }
import MyComponent from './components/MyComponent'
Vue.use(MyComponent)

// use element-ui
Vue.use(ElementUI, { size: 'mini', zIndex: 3000 })
// register component to use
Vue.component('v-chart', ECharts)

Vue.config.productionTip = false

import * as filters from './filters' // global filters
// 注册全局过滤器
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
