package cn.rpc.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 15:28
 * @Author: 阿左不是蜗牛
 * @Description: 封装发起Rpc调用的请求
 */
@Data
public class RpcRequest implements Serializable {
    /**
     * 定义请求的唯一标识，用于异步回调关联响应
     */
    private String  requestId;

    /**
     * 请求的服务名称
     */
    private String serviceName;

    /**
     * 请求调用的方法
     */
    private String method;

    private Map<String, String> headers = new HashMap<>();

    private Class<?>[] parameterTypes;

    private Object[] parameters;

}
