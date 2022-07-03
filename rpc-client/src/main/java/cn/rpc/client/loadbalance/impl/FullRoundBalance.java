package cn.rpc.client.loadbalance.impl;

import cn.rpc.client.loadbalance.LoadBalance;
import cn.rpc.common.model.Service;

import java.util.List;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 19:42
 * @Author: 阿左不是蜗牛
 * @Description: 轮询
 */
public class FullRoundBalance implements LoadBalance {

    private int index;

    @Override
    public synchronized Service chooserOne(List<Service> services) {
        // 加锁防止多线程情况下，index超出service.size()
        if (index == services.size()) {
            index = 0;
        }
        return services.get(index++);
    }
}
