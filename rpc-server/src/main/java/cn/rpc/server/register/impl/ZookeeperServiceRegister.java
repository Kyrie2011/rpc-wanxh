package cn.rpc.server.register.impl;

import cn.rpc.common.model.Service;
import cn.rpc.common.serializer.ZooKeeperSerializer;
import cn.rpc.server.register.DefaultServiceRegister;
import cn.rpc.server.register.ServiceObject;
import cn.rpc.server.register.ServiceRegister;
import com.alibaba.fastjson2.JSON;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.channels.Pipe;

import static cn.rpc.common.constant.RpcConstant.*;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 22:04
 * @Author: 阿左不是蜗牛
 * @Description: ZK服务注册器，提供服务注册、暴露服务的能力
 */
public class ZookeeperServiceRegister extends DefaultServiceRegister {

    private ZkClient zkClient;

    public ZookeeperServiceRegister(String zkAddress, Integer port, String protocol, Integer weight) {
        this.zkClient = new ZkClient(zkAddress);
        zkClient.setZkSerializer( new ZooKeeperSerializer());
        this.port = port;
        this.protocol = protocol;
        this.weight = weight;
    }

    /**
     *
     * 服务注册
     * @param serviceObject
     * @throws Exception
     */
    @Override
    public void register(ServiceObject serviceObject) throws Exception {
        super.register(serviceObject);
        Service service = new Service();
        String host = InetAddress.getLocalHost().getHostAddress();
        String address = host + ":" + port;
        service.setServiceAddress(address);
        service.setServiceProtocol(protocol);
        service.setWeight(this.weight);


    }

    /**
     * 服务暴露（把服务信息保存到Zookeeper上）
     * @param service
     */
    private void exportService(Service service){
        String serviceName = service.getServiceName();

        String uri = JSON.toJSONString(service);

        try {
            uri = URLEncoder.encode(uri, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String servicePath = ZK_SERVICE_PATH + PATH_DELIMITER + serviceName + SERVICE_TAG;
        if (!zkClient.exists(servicePath)) {
            // 没有该节点就创建(持久化节点，路径前缀)
            zkClient.createPersistent(servicePath, true);
        }

        String uriPath = servicePath + PATH_DELIMITER + uri;

        if (zkClient.exists(uriPath)) {
            // 删除之前的节点
            zkClient.delete(uriPath);
        }

        /**
         * 为服务创建一个临时节点，会话失效即被清理
         * 将指定ServiceObject对象序列化后保存到ZK上，供客户端发现
         */
        zkClient.createEphemeral(uriPath);
    }
}
