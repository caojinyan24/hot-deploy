#hot-deploy
测试url
http://localhost:8081/swa/upload?fileName=fileName.properties&value=2
http://127.0.0.1:8081/getData?fileName=fileName.properties
http://localhost:8080/swa/load


#功能介绍
* 文件（key-value）动态加载（通过http请求）
* 实现集群管理，服务自动上下线（zookeeper）



#tip1
目前zookeeper保存的数据（serverList）发生变更的时候，把更新后的数据保存在redis中，consumer每次从redis中获取当前可用的serverList
（consumer端可通过获得zookeeper连接取得节点数据，即请求服务器地址（无需保存在redis中））


#tip2
spring自定义注解
进程间通信
servlet，servlet-mapping


