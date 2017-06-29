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