package cn.rpc.client.net;

import java.util.concurrent.*;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 18:26
 * @Author: 阿左不是蜗牛
 * @Description: TODO
 */
public class RpcFuture<T> implements Future<T> {

    private T response;

    /**
     * 请求和响应一一对应
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * Future的请求时间，用于计算Future是否超时
     */
    private long beginTime = System.currentTimeMillis();

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        if (response != null){
            return true;
        }
        return false;
    }

    /**
     * 获取响应，直到有结果才返回
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public T get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return response;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (countDownLatch.await(timeout, unit)){
            return response;
        }
        return null;
    }

    public void setResponse(T response){
        this.response = response;
        countDownLatch.countDown();
    }
    public long getBeginTime(){
        return beginTime;
    }
}
