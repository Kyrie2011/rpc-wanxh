package cn.rpc.client.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 15:19
 * @Author: 阿左不是蜗牛
 * @Description: 请求发送处理类
 */
public class ClientRequestHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ClientRequestHandler.class);

    private CountDownLatch countDownLatch;

    private Object readMsg;

    private byte[] data;

    public ClientRequestHandler(byte[] data){
        countDownLatch = new CountDownLatch(1);
        this.data = data;
    }

    /**
     * 连接服务端成功后发送数据
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Connect to server successfully : {}", ctx);
        ByteBuf reqBuf = Unpooled.buffer(data.length);  // netty的zero copy
        reqBuf.writeBytes(data);
        logger.debug("Client sends message:{}", reqBuf);
        // 发送数据到服务端
        ctx.writeAndFlush(reqBuf);

    }

    /**
     * 读取数据，数据读取完毕释放CountDownLatch锁
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("Client reads message:{}", msg);
        // 接收服务端发送过来的数据
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] resp = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(resp);
        // 手动回收
        ReferenceCountUtil.release(byteBuf);
        this.readMsg = resp;
        countDownLatch.countDown();
    }

    public Object respData() throws InterruptedException {
        countDownLatch.await();
        return readMsg;
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
}
