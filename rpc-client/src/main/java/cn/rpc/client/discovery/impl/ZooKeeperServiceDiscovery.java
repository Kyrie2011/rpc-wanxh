package cn.rpc.client.discovery.impl;
import cn.rpc.client.discovery.ServiceDiscovery;
import cn.rpc.common.constant.RpcConstant;
import cn.rpc.common.model.Service;
import cn.rpc.common.serializer.ZooKeeperSerializer;
import com.alibaba.fastjson2.JSON;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * 通过服务名称获取服务列表(服务发现)
     * @param name
     * @return List<Service>
     */
    @Override
    public List<Service> findServiceList(String name) {
        String servicePath = RpcConstant.ZK_SERVICE_PATH + RpcConstant.PATH_DELIMITER + name + RpcConstant.SERVICE_TAG;
        List<String> children = zkClient.getChildren(servicePath);
        return Optional.ofNullable(children).orElse(new ArrayList<>()).stream().map( str -> {
            String serviceStr = null;
            try {
                serviceStr = URLDecoder.decode(str, RpcConstant.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 解析成Service对象
            return JSON.parseObject(serviceStr, Service.class);
        }).collect(Collectors.toList());
    }
}
