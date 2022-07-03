package cn.rpc.common.util;



import cn.rpc.common.exception.RpcException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

/**
 * 泛化调用接口，由于没有目标类，复杂参数对象采用Map进行传输
 * 泛型调用不会出现Callback
 */
public interface GenericService {

    /**
     * 判断是否是泛化接口
     */
    Predicate<Class<?>> GENERIC = cls -> cls.isInterface() && GenericService.class.isAssignableFrom(cls);

    /**
     * 泛化调用，和老接口兼容
     *
     * @param method         方法名
     * @param parameterTypes 参数类型
     * @param args           参数列表
     * @return 返回值
     */
    default Object $invoke(final String method, final String[] parameterTypes, final Object[] args) {
        try {
            return $async(method, parameterTypes, args).get(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RpcException(String.format("Failed invoking %s, It's interrupted.", method));
        } catch (ExecutionException e) {
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            if (throwable instanceof RpcException) {
                throw (RpcException) throwable;
            }
            throw new RpcException(String.format("Failed invoking %s, caused by %s", method, throwable.getMessage()));
        } catch (TimeoutException e) {
            throw new RpcException(String.format("Failed invoking %s, It's timeout.", method));
        }
    }

    /**
     * 异步泛化调用
     *
     * @param method         方法名
     * @param parameterTypes 参数类型
     * @param args           参数列表
     * @return 返回值
     */
    CompletableFuture<Object> $async(String method, String[] parameterTypes, Object[] args);

}
