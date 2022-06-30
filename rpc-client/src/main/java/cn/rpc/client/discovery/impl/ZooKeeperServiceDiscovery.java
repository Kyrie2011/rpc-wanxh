package cn.rpc.client.discovery.impl;
import cn.rpc.client.discovery.ServiceDiscovery;
import cn.rpc.common.model.Service;
import cn.rpc.common.serializer.ZooKeeperSerializer;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @program: rpc-wanxh
 * @Date: 2022/6/30 23:38
 * @Author: 阿左不是蜗牛
 * @Description: ZK作为注册中心的实现
 */
public class ZooKeeperServiceDiscovery implements ServiceDiscovery {

    private ZkClient zkClient;

    // 构造函数
    public ZooKeeperServiceDiscovery(String zkAddress){
        zkClient = new ZkClient(zkAddress);
        zkClient.setZkSerializer(new ZooKeeperSerializer());
    }

    @Override
    public List<Service> findServiceList(String name) {
        return null;
    }
}
