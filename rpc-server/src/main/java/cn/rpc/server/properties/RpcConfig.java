package cn.rpc.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 23:33
 * @Author: 阿左不是蜗牛
 * @Description: RPC属性配置
 */
@ConfigurationProperties(prefix = "cn.rpc")
public class RpcConfig {

    /**
     * 服务注册中心地址
     */
    private String registerAddress = "127.0.0.1";

    /**
     * 服务暴露端口
     */
    private Integer serverPort = 8888;

    /**
     * 服务协议
     */
    private String protocol = "java";

    /**
     * 负载均衡算法
     */
    private String loadBalance = "random";

    /**
     * 权重，默认为 1
     */
    private Integer weight = 1;

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
