# hot-deploy
http://localhost:8081/swa/upload?fileName=fileName.properties&value=2
http://127.0.0.1:8081/getData?fileName=fileName.properties

http://localhost:8080/swa/load






#todo：
* 实现集群管理
服务注册：通过配置文件
client连接：
提供watcher服务


client端和zookeeper断开连接后，去掉节点数据（实现自动下线服务）
#怎么在client端通过spring来调用
存在兼容问题（必须要使用spring框架，不利于移植）？
自动发现服务器ip和port，自动注册节点，(程序启动的时候自动执行)


当client重启后，zookeeper重新注册节点，但获取数据的时候，依旧是null（通过命令行删除节点，但client端的节点数据没有更新）


