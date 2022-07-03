package cn.rpc.client.loadbalance;

import cn.rpc.common.model.Service;

import java.util.List;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 17:27
 * @Author: 阿左不是蜗牛
 * @Description: 客户端负载均衡接口类
 */
public interface LoadBalance {
    /**
     * 负载均衡
     * @param services
     * @return
     */
    Service chooserOne(List<Service> services);
}
