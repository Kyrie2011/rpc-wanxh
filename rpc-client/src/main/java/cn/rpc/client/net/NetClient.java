package cn.rpc.client.net;

import cn.rpc.common.model.RpcRequest;
import cn.rpc.common.model.RpcResponse;
import cn.rpc.common.model.Service;
import cn.rpc.common.protocol.MessageProtocol;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 15:14
 * @Author: 阿左不是蜗牛
 * @Description: 网络请求客户端，定义网络请求规范
 */
public interface NetClient {
    byte[] sendRequest(byte[] data, Service service) throws InterruptedException;

    RpcResponse sendRequest(RpcRequest rpcRequest, Service service, MessageProtocol messageProtocol);
}
