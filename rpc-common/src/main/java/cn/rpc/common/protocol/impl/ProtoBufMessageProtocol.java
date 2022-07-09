package cn.rpc.common.protocol.impl;

import cn.rpc.common.annotation.MessageProtocolAno;
import cn.rpc.common.constant.RpcConstant;
import cn.rpc.common.model.RpcRequest;
import cn.rpc.common.model.RpcResponse;
import cn.rpc.common.protocol.MessageProtocol;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/9 21:41
 * @Author: 阿左不是蜗牛
 * @Description: Protobuf序列化协议
 */
@MessageProtocolAno(RpcConstant.PROTICOL_PROTOBUF)
public class ProtoBufMessageProtocol implements MessageProtocol {
    @Override
    public byte[] marshallingRequest(RpcRequest rpcRequest) throws Exception {
        return new byte[0];
    }

    @Override
    public RpcRequest unmarshallingRequest(byte[] data) throws Exception {
        return null;
    }

    @Override
    public byte[] marshallingResponse(RpcResponse rpcResponse) throws Exception {
        return new byte[0];
    }

    @Override
    public RpcResponse unmarshallingResponse(byte[] data) throws Exception {
        return null;
    }
}
