package cn.rpc.server.register;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 21:34
 * @Author: 阿左不是蜗牛
 * @Description: 服务注册，定义服务注册规范
 */
public interface ServiceRegister {
    void register(ServiceObject serviceObject) throws Exception;

    ServiceObject getServiceObject(String name) throws Exception;
}
