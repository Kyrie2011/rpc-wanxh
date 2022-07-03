package cn.rpc.common.model;

import cn.rpc.common.constant.RpcStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 15:27
 * @Author: 阿左不是蜗牛
 * @Description: 封装Rpc调用的响应结果
 */
public class RpcResponse implements Serializable {
    private String requestId;

    private Map<String, String> headers = new HashMap<>();

    /**
     * 返回结果值
     */
    private Object returnValue;

    private Exception exception;

    /**
     * 调用的状态及响应码
     */
    private RpcStatusEnum rpcStatus;

    public RpcResponse() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public RpcResponse(RpcStatusEnum rpcStatus) {
        this.rpcStatus = rpcStatus;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public RpcStatusEnum getRpcStatus() {
        return rpcStatus;
    }

    public void setRpcStatus(RpcStatusEnum rpcStatus) {
        this.rpcStatus = rpcStatus;
    }
}
