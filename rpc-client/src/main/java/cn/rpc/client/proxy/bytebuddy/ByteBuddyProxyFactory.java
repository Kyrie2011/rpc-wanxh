package cn.rpc.client.proxy.bytebuddy;

import cn.rpc.client.proxy.ProxyFactory;
import cn.rpc.common.exception.ProxyException;

import java.lang.reflect.InvocationHandler;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 21:01
 * @Author: 阿左不是蜗牛
 * @Description: TODO
 */
public class ByteBuddyProxyFactory implements ProxyFactory {
    @Override
    public <T> T getProxy(Class<T> clz, InvocationHandler invoker) throws ProxyException {
        return ProxyFactory.super.getProxy(clz, invoker);
    }

    @Override
    public <T> T getProxy(Class<T> clz, InvocationHandler invoker, ClassLoader classLoader) throws ProxyException {
        return null;
    }
}
