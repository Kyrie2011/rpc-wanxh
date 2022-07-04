package cn.rpc.server.net;

import cn.rpc.server.RpcServer;
import cn.rpc.server.handler.ServerRequestHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 23:03
 * @Author: 阿左不是蜗牛
 * @Description: 服务端网络实现，提供Netty网络服务开启、关闭的能力
 */
public class NettyRpcServer extends RpcServer {

    private static Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);

    private Channel channel;

    private static final ExecutorService pool = new ThreadPoolExecutor(4, 8,
            200, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadFactoryBuilder().setNameFormat("rpcServer-%d").build());

    public NettyRpcServer(int port, String protocol, ServerRequestHandler requestHandler) {
        super(port, protocol, requestHandler);
    }

    @Override
    public void start() {
        // 配置服务端
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 启动服务
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置两个线程组bossGroup和workGroup
            serverBootstrap.group(bossGroup, workerGroup)
                    // 设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    // backlog 指定了内核为”此套接字“排队的最大连接个数
                    // 对于给定的监听套接字，内核要维护两个队列：未连接队列和已连接队列
                    // backlog的值即为“未连接队列”和“已连接队列”的和
                    // 如果大于队列的最大长度，请求会被拒绝
                    .option(ChannelOption.SO_BACKLOG, 128)  // 设置最大连接个数
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 使用匿名内部类的形式初始化通道对象
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    ChannelPipeline pipeline = socketChannel.pipeline();
                                    // 给pipeline管段设置处理器
                                    pipeline.addLast(new ChannelRequestHandler());
                                }
                            }
                    );
            // 绑定端口号，启动服务端
            ChannelFuture future = serverBootstrap.bind(port).sync();
            logger.debug("Server started successfully");
            channel = future.channel();
            // 等待服务通道关闭
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("start netty sever failed,msg:{}", e.getMessage());
        } finally {
            // 释放线程组资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    @Override
    public void stop() {
        if (this.channel != null){
            this.channel.close();
        }
    }

    /**
     * @program: rpc-wanxh
     * @Date: 2022/7/3 23:21
     * @Author: 阿左不是蜗牛
     * @Description: Channel通道处理器
     */
    private class ChannelRequestHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.debug("Channel active :{}", ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 线程池处理
            pool.submit(() -> {
                try {
                    logger.debug("the server receives message :{}", msg);
                    ByteBuf byteBuf = (ByteBuf) msg;
                    // 读取消息数据, 写入reqData, 回收byteBuf
                    byte[] reqData = new byte[byteBuf.readableBytes()];
                    byteBuf.readBytes(reqData);
                    // 手动回收
                    ReferenceCountUtil.release(byteBuf);
                    // 处理请求
                    byte[] responseData = requestHandler.handleRequest(reqData);
                    ByteBuf respBuf = Unpooled.buffer(responseData.length);
                    // 输出响应
                    respBuf.writeBytes(responseData);
                    logger.debug("Send response:{}", respBuf);
                    // 写出响应消息
                    ctx.writeAndFlush(respBuf);
                }catch (Exception e){
                    logger.error("server read exception", e);
                }
            });

        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            logger.error("Exception occurred:{}", cause.getMessage());
            ctx.close();
        }
    }


}
