package cn.rpc.client.loadbalance.impl;

import cn.rpc.client.loadbalance.LoadBalance;
import cn.rpc.common.model.Service;

import java.util.List;
import java.util.Random;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 19:40
 * @Author: 阿左不是蜗牛
 * @Description: 随机选择
 */
public class RandomBalance implements LoadBalance {

    private static Random random = new Random();

    @Override
    public Service chooserOne(List<Service> services) {
        return services.get(random.nextInt(services.size()));
    }
}
