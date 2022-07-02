package cn.rpc.common.protocol.impl;

import cn.rpc.common.model.RpcRequest;
import cn.rpc.common.model.RpcResponse;
import cn.rpc.common.protocol.MessageProtocol;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 16:04
 * @Author: 阿左不是蜗牛
 * @Description: JDK序列化消息协议
 */
public class JdkSerializeMessageProtocol implements MessageProtocol {

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(obj);
        return outputStream.toByteArray();
    }


    @Override
    public byte[] marshallingRequest(RpcRequest rpcRequest) throws Exception {
        return this.serialize(rpcRequest);
    }

    @Override
    public RpcRequest unmarshallingRequest(byte[] data) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        return (RpcRequest) objectInputStream.readObject();
    }

    @Override
    public byte[] marshallingResponse(RpcResponse rpcResponse) throws Exception {
        return this.serialize(rpcResponse);
    }

    @Override
    public RpcResponse unmarshallingResponse(byte[] data) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        return (RpcResponse) objectInputStream.readObject();
    }

    /**
     * 对比各种序列化协议比较他们的性能
     * @param args
     * @throws Exception
     * TODO
     */
    public static void main(String[] args) throws Exception{
        final int objectCount = 10000;
        List<RpcRequest> list = new ArrayList<>(objectCount);
        for (int i = 0; i < objectCount; i++) {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setServiceName("service" + i);
        }
    }
}
