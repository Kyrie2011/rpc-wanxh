package cn.rpc.server.handler;

import cn.rpc.common.constant.RpcStatusEnum;
import cn.rpc.common.model.RpcRequest;
import cn.rpc.common.model.RpcResponse;
import cn.rpc.common.protocol.MessageProtocol;
import cn.rpc.server.register.ServiceObject;
import cn.rpc.server.register.ServiceRegister;

import java.lang.reflect.Method;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 22:42
 * @Author: 阿左不是蜗牛
 * @Description: 服务端，请求处理者，提供解组请求、编组响应等操作
 */
public class ServerRequestHandler {

    private MessageProtocol messageProtocol;

    private ServiceRegister serviceRegister;

    public ServerRequestHandler(MessageProtocol messageProtocol, ServiceRegister serviceRegister) {
        this.messageProtocol = messageProtocol;
        this.serviceRegister = serviceRegister;
    }

    /**
     * 处理接收到的请求
     * @param data
     * @return
     * @throws Exception
     */
    public byte[] handleRequest(byte[] data) throws Exception {
        // 1. 解组消息
        RpcRequest request = this.messageProtocol.unmarshallingRequest(data);

        // 2. 查找服务对应
        ServiceObject serviceObject = serviceRegister.getServiceObject(request.getServiceName());

        RpcResponse response = null;
        if (serviceObject == null) {
            response = new RpcResponse(RpcStatusEnum.NOT_FOUND);
        }else{
            try {
                // 3. 反射调用对应的方法
                Method method = serviceObject.getClazz().getMethod(request.getMethod(), request.getParameterTypes());
                Object returnValue = method.invoke(serviceObject.getObj(), request.getParameters());
                response = new RpcResponse(RpcStatusEnum.SUCCESS);
                response.setReturnValue(returnValue);
            }catch (Exception e){
                response = new RpcResponse(RpcStatusEnum.ERROR);
                response.setException(e);
            }
        }
        // 4. 编组响应消息
        response.setRequestId(request.getRequestId());
        return this.messageProtocol.marshallingResponse(response);
    }


    public MessageProtocol getMessageProtocol() {
        return messageProtocol;
    }

    public void setMessageProtocol(MessageProtocol messageProtocol) {
        this.messageProtocol = messageProtocol;
    }

    public ServiceRegister getServiceRegister() {
        return serviceRegister;
    }

    public void setServiceRegister(ServiceRegister serviceRegister) {
        this.serviceRegister = serviceRegister;
    }
}
