
const MyComponent = {}

MyComponent.install = function(Vue) {
  Vue.component('vPagination', () => import('./pagination/Pagination'))
}
export default MyComponent
