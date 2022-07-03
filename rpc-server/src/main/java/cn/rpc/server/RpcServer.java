package cn.rpc.server;

import cn.rpc.server.handler.ServerRequestHandler;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 21:28
 * @Author: 阿左不是蜗牛
 * @Description: Rpc服务端抽象类
 */
public abstract class RpcServer {

    /**
     * 服务端口
     */
    protected int port;


    /**
     * 服务协议
     */
    protected String protocol;

    /**
     * 请求处理者
     */
    protected ServerRequestHandler requestHandler;


    public RpcServer(int port, String protocol, ServerRequestHandler requestHandler) {
        this.port = port;
        this.protocol = protocol;
        this.requestHandler = requestHandler;
    }

    /**
     * 开启服务
     */
    public abstract void start();

    /**
     * 关闭服务
     */
    public abstract void stop();

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public ServerRequestHandler getRequestHandler() {
        return requestHandler;
    }

    public void setRequestHandler(ServerRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }
}
