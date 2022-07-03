package cn.rpc.server.register;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 21:57
 * @Author: 阿左不是蜗牛
 * @Description: 默认服务注册器
 */
public abstract class DefaultServiceRegister implements ServiceRegister{

    private Map<String, ServiceObject> servicetMap = new HashMap<>();

    /**
     * 协议名称
     */
    protected String protocol;

    /**
     * 服务端口号
     */
    protected Integer port;

    /**
     * 权重
     */
    protected Integer weight;


    /**
     * 具体实现交由下层具体子类去实现
     * @param serviceObject
     * @throws Exception
     */
    @Override
    public void  register(ServiceObject serviceObject) throws Exception {
        if (serviceObject == null) {
            throw new IllegalArgumentException("parameter cannot be empty");
        }
        servicetMap.put(serviceObject.getName(), serviceObject);

    }

    @Override
    public ServiceObject getServiceObject(String name) throws Exception {
        return servicetMap.get(name);
    }
}
