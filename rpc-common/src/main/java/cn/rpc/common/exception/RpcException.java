package cn.rpc.common.exception;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 19:01
 * @Author: 阿左不是蜗牛
 * @Description: Rpc调用异常封装
 */
public class RpcException extends RuntimeException{
    public RpcException(String message){
        super(message);
    }
}
