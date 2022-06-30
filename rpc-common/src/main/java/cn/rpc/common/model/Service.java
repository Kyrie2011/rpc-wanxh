package cn.rpc.common.model;

import lombok.Data;

/**
 * @program: rpc-wanxh
 * @Date: 2022/6/30 23:40
 * @Author: 阿左不是蜗牛
 * @Description: 服务model
 */
@Data
public class Service {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务协议
     */
    private String serviceProtocol;

    /**
     * 服务地址(格式：ip:port)
     */
    private String serviceAddress;

    /**
     * 权重，越大优先级越高
     */
    private Integer weight;
}
