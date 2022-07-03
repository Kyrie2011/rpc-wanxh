package cn.rpc.server.register.impl;

import cn.rpc.server.register.DefaultServiceRegister;
import cn.rpc.server.register.ServiceObject;


/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 22:06
 * @Author: 阿左不是蜗牛
 * @Description: Nacos作为注册中心
 */
public class NacosServiceRegister extends DefaultServiceRegister {
    @Override
    public void register(ServiceObject serviceObject) throws Exception {

    }

    @Override
    public ServiceObject getServiceObject(String name) throws Exception {
        return null;
    }
}
