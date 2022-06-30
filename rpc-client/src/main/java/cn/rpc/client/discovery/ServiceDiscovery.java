package cn.rpc.client.discovery;

import cn.rpc.common.model.Service;

import java.util.List;

/**
 * @program: rpc-wanxh
 * @Date: 2022/6/30 23:36
 * @Author: 阿左不是蜗牛
 * @Description: 服务发现
 */
public interface ServiceDiscovery {
    List<Service> findServiceList(String name);
}
