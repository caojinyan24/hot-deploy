package swa.zookeeper;

import swa.zookeeper.RegistryService;

/**
 * Created by jinyan on 6/19/17.
 */
public interface RegistryService {
    public void register(String data);//注册完成之后，创建一个新的zk节点

}
