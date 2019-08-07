export function getFormRules() {
  return {
    name: [
      { required: true, message: '填写名称', trigger: 'blur' }
    ],
    url: [
      { required: true, message: '填写请求路径', trigger: 'blur' }
    ],
    serviceName: [
      { required: true, message: '填写服务名', trigger: 'blur' }
    ],
    methodName: [
      { required: true, message: '填写方法', trigger: 'blur' }
    ]
  }
}
