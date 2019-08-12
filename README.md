
### 数据库选择
 * 在`gateway-ops/src/main/resource/application.properties`中的`gateway_db_type`可以指定db类型,目前支持h2和MySQL类型.
 * 默认使用h2
 
### 前端部分
- 使用[vue.js](https://vuejs.org)作为前端框架
- 设置 npm **代理镜像** : 可以设置npm代理镜像来加速npm install的过程：在~/.npmrc中增加 `registry =https://registry.npm.taobao.org`

### 生产环境配置
1. 下载代码: `https://github.com/geekfoxer/geekfoxer-gateway.git`
2. 在`gateway-server/src/main/resource/application.properties`中指定注册中心地址
3. 构建
    > - `mvn clean package -DskipTests`
4. 启动
    * `cd gateway-distribution/target;  java -jar gateway-ops-1.0-SNAPSHOT.jar; java -jar gateway-ops-1.0-SNAPSHOT.jar`
5. 访问 `http://127.0.0.1:8096`

