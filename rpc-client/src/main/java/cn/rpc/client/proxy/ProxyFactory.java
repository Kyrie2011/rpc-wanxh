package cn.rpc.client.proxy;

import cn.rpc.common.exception.ProxyException;
import cn.rpc.common.util.ClassUtils;

import java.lang.reflect.InvocationHandler;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 17:31
 * @Author: 阿左不是蜗牛
 * @Description: 客户端代理工厂抽象类, TODO
 */
public interface ProxyFactory {

    default <T> T getProxy(final Class<T> clz, final InvocationHandler invoker) throws ProxyException {
        return getProxy(clz, invoker, ClassUtils.getCurrentClassLoader());
    }

    <T> T getProxy(Class<T> clz, InvocationHandler invoker, ClassLoader classLoader) throws ProxyException;



}
