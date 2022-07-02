package cn.rpc.common.protocol;

import cn.rpc.common.model.RpcRequest;
import cn.rpc.common.model.RpcResponse;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 15:49
 * @Author: 阿左不是蜗牛
 * @Description: 消息协议：定义编组请求、解组请求；编组响应、解组响应的规范
 */
public interface MessageProtocol {

    /**
     * 编组请求
     * @param rpcRequest 请求信息
     * @return 请求信息编组后的字节数组
     * @throws Exception
     */
    byte[] marshallingRequest(RpcRequest rpcRequest) throws Exception;

    /**
     * 解组请求
     * @param data
     * @return 字节数组解组后的请求信息
     * @throws Exception
     */
    RpcRequest unmarshallingRequest(byte[] data) throws Exception;

    /**
     * 编组响应
     * @param rpcResponse
     * @return
     * @throws Exception
     */
    byte[] marshallingResponse(RpcResponse rpcResponse) throws Exception;

    /**
     * 解组响应
     * @param data
     * @return
     * @throws Exception
     */
    RpcResponse unmarshallingResponse(byte[] data) throws Exception;
}
