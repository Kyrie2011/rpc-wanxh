package cn.rpc.client.proxy;

import cn.rpc.client.cache.ServiceDiscoveryCache;
import cn.rpc.client.discovery.ServiceDiscovery;
import cn.rpc.client.loadbalance.LoadBalance;
import cn.rpc.client.net.NetClient;
import cn.rpc.common.exception.RpcException;
import cn.rpc.common.model.RpcRequest;
import cn.rpc.common.model.RpcResponse;
import cn.rpc.common.model.Service;
import cn.rpc.common.protocol.MessageProtocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 15:21
 * @Author: 阿左不是蜗牛
 * @Description: 客户端代理工厂，用于创建远程服务的代理类
 * 封装编组请求、请求发送、编组响应等操作
 */
public class ClientProxyFactory {

    private ServiceDiscovery serviceDiscovery;

    private NetClient netClient;

    private Map<String, MessageProtocol> supportMessageProtocols;

    private Map<Class<?>, Object> objectCache = new ConcurrentHashMap<>();

    private LoadBalance loadBalance;

    /**
     * 通过jdk的动态代理
     * @param clazz
     * @param <T>
     * @return 服务代理类实例
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T) objectCache.computeIfAbsent(clazz, clz -> Proxy.newProxyInstance(
                clz.getClassLoader(), new Class[]{clz}, new ClientInvocationHandler(clz))
        );
    }

    /**
     * handler的具体实现类
     */
    private class ClientInvocationHandler implements InvocationHandler{

        private Class<?> clazz;

        public ClientInvocationHandler(Class<?> clazz){
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (method.getName().equals("toString")){
                return proxy.toString();
            }

            if (method.getName().equals("hashCode")){
                return 0;
            }
            // 1. 获取服务信息
            String serviceName = clazz.getName();
            List<Service> serviceList = getServiceList(serviceName);

            // 2. 客户端负载均衡
            Service service = loadBalance.chooserOne(serviceList);

            // 3. 构造request对象
            RpcRequest request = new RpcRequest();
            request.setRequestId(UUID.randomUUID().toString());
            request.setServiceName(service.getServiceName()); // 服务名称
            request.setMethod(method.getName());  // 方法名称
            request.setParameters(args); // 请求参数
            request.setParameterTypes(method.getParameterTypes());  // 参数类型

            // 4, 协议层编组
            MessageProtocol messageProtocol = supportMessageProtocols.get(service.getServiceProtocol());

            // 4.1 编组请求
            // byte[] marshallingRequest = messageProtocol.marshallingRequest(request);
            // 5. 调用网络层发送请求
            // byte[] respData = netClient.sendRequest(marshallingRequest, service);
            // 6. 解组响应数据
            // RpcResponse response1 = messageProtocol.unmarshallingResponse(respData);

            RpcResponse response = netClient.sendRequest(request, service, messageProtocol);
            if (response == null) {
                throw new RpcException("the response is null");
            }

            // 7. 结果处理
            if (response.getException() != null) {
                return response.getException();
            }
            return response.getReturnValue();
        }
    }

    /**
     * 根据服务名称获取可用的服务地址列表
     * @param serviceName
     */
    private List<Service> getServiceList(String serviceName) {
        List<Service> services;
        synchronized (serviceName){   // TODO 加锁
            // 是否有本地缓存
            if (ServiceDiscoveryCache.isEmpty(serviceName)){
                services = serviceDiscovery.findServiceList(serviceName);
                if (services == null || services.size() == 0){
                    throw new RpcException("No provider available!");

                }
                ServiceDiscoveryCache.put(serviceName, services);  // 若子节点发生改动，则会清空缓存
            }else{
                services = ServiceDiscoveryCache.get(serviceName);
            }
        }
        return services;
    }

    public ServiceDiscovery getServiceDiscovery() {
        return serviceDiscovery;
    }

    public void setServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public NetClient getNetClient() {
        return netClient;
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

    public Map<String, MessageProtocol> getSupportMessageProtocols() {
        return supportMessageProtocols;
    }

    public void setSupportMessageProtocols(Map<String, MessageProtocol> supportMessageProtocols) {
        this.supportMessageProtocols = supportMessageProtocols;
    }

    public Map<Class<?>, Object> getObjectCache() {
        return objectCache;
    }

    public void setObjectCache(Map<Class<?>, Object> objectCache) {
        this.objectCache = objectCache;
    }

    public LoadBalance getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }
}
