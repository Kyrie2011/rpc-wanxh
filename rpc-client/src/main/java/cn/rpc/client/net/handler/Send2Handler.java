package cn.rpc.client.net.handler;

import cn.rpc.client.net.RpcFuture;
import cn.rpc.client.net.impl.NettyNetClient;
import cn.rpc.common.exception.RpcException;
import cn.rpc.common.model.RpcRequest;
import cn.rpc.common.model.RpcResponse;
import cn.rpc.common.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 18:21
 * @Author: 阿左不是蜗牛
 * @Description: 发送请求处理类
 */
public class Send2Handler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(Send2Handler.class);

    /**
     * 等待通道建立最大时间
     */
    public static final int CHANNEL_WAIT_TIME = 4;

    /**
     * 等待响应最大时间
     */
    public static final int RESPONSE_WAIT_TIME = 8;

    private volatile Channel channel;

    private String remoteAddress;

    private static Map<String, RpcFuture<RpcResponse>> requestMap = new ConcurrentHashMap<>();

    private MessageProtocol messageProtocol;

    private CountDownLatch latch = new CountDownLatch(1);

    public Send2Handler(MessageProtocol messageProtocol, String remoteAddress){
        this.messageProtocol = messageProtocol;
        this.remoteAddress = remoteAddress;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
        latch.countDown();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Connect to server successfully:{}", ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Client reads message:{}", msg);
        ByteBuf byteBuf = (ByteBuf) msg;

        byte[] resp = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(resp);
        // 手动回收
        ReferenceCountUtil.release(byteBuf);
        RpcResponse rpcResponse = messageProtocol.unmarshallingResponse(resp);
        RpcFuture<RpcResponse> rpcFuture = requestMap.get(rpcResponse.getRequestId());
        rpcFuture.setResponse(rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        logger.error("Exception occurred:{}", cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.error("channel inactive with remoteAddress: [{}]", remoteAddress);
        // 从已连接的缓存中移除
        NettyNetClient.connectedServerNodes.remove(remoteAddress);
    }

    public RpcResponse sendRequest(RpcRequest request){
        RpcResponse response;
        RpcFuture<RpcResponse> rpcFuture = new RpcFuture<>();
        requestMap.put(request.getRequestId(), rpcFuture);

        try {
            // 根据序列化协议，进行编组
            byte[] data = messageProtocol.marshallingRequest(request);
            ByteBuf reqBuf = Unpooled.buffer(data.length);
            reqBuf.writeBytes(data);
            if (latch.await(CHANNEL_WAIT_TIME, TimeUnit.SECONDS)) {
                // 发送数据到服务端
                channel.writeAndFlush(reqBuf);
                // 等待响应 (待channelRead中设置response)
                response = rpcFuture.get(RESPONSE_WAIT_TIME, TimeUnit.SECONDS);
            }else{
                throw new RpcException("establish channel time out");
            }

        } catch (Exception e) {
            throw new RpcException(e.getMessage());
        }finally {
            // 及时移除
            requestMap.remove(request.getRequestId());
        }

        return response;
    }
}
