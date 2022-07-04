package cn.rpc.client.net.impl;

import cn.rpc.client.net.NetClient;
import cn.rpc.client.net.handler.ClientRequest2Handler;
import cn.rpc.client.net.handler.ClientRequestHandler;
import cn.rpc.common.model.RpcRequest;
import cn.rpc.common.model.RpcResponse;
import cn.rpc.common.model.Service;
import cn.rpc.common.protocol.MessageProtocol;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 15:18
 * @Author: 阿左不是蜗牛
 * @Description: 基于Netty实现网络请求
 */
public class NettyNetClient implements NetClient {

    private static Logger logger = LoggerFactory.getLogger(NettyNetClient.class);

    /**
     * 定义线程池
     */
    private static ExecutorService threadPool = new ThreadPoolExecutor(
            4, 10,
            200, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadFactoryBuilder().setNameFormat("rpcClient-%d").build());


    private EventLoopGroup loopGroup = new NioEventLoopGroup(4);

    /**
     * 已连接的服务缓存
     * key：服务地址，格式：ip:port
     */
    public static Map<String, ClientRequest2Handler> connectedServerNodes = new ConcurrentHashMap<>();

    @Override
    public byte[] sendRequest(byte[] data, Service service) throws InterruptedException {
        String address = service.getServiceAddress();

        String[] ipAndPort = address.split(":");
        final String serverAddress = ipAndPort[0];
        final String serverPort = ipAndPort[1];
        ClientRequestHandler sendHandler = new ClientRequestHandler(data);
        byte[] respData;
        // 配置客户端
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            // 客户端启动类
            Bootstrap bootstrap = new Bootstrap();
            // 设置线程组
            bootstrap.group(loopGroup)
                    // 设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(sendHandler);
                        }
                    });
            // 启动客户端连接
            bootstrap.connect(serverAddress, Integer.parseInt(serverPort)).sync();

            respData = (byte[]) sendHandler.respData();
            logger.debug("SendRequest get reply: {}", respData);
        } finally {
            eventExecutors.shutdownGracefully();
        }

        return respData;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest rpcRequest, Service service, MessageProtocol messageProtocol) {
        String address = service.getServiceAddress();

        synchronized (address) { // TODO 代码块要加锁
            // 已连接服务的缓存
            if (connectedServerNodes.containsKey(address)) {
                ClientRequest2Handler handler = connectedServerNodes.get(address);
                logger.info("----使用现有的连接----");
                return handler.sendRequest(rpcRequest);
            }

            String[] ipAndPort = address.split(":");
            final String serverAddress = ipAndPort[0];
            final String serverPort = ipAndPort[1];
            ClientRequest2Handler handler = new ClientRequest2Handler(messageProtocol, address);
            // 线程池提交
            threadPool.submit(() -> {
                // 客户端启动类
                Bootstrap bootstrap = new Bootstrap();
                // 设置线程组
                bootstrap.group(loopGroup)
                        // 设置客户端的通道实现类型
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(handler);
                            }
                        });
                // 启动客户端连接
                ChannelFuture channelFuture = bootstrap.connect(serverAddress, Integer.parseInt(serverPort));

                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        connectedServerNodes.put(address, handler);
                    }
                });

            });
            logger.info("使用新的连接......");
            // 发送请求
            return handler.sendRequest(rpcRequest);
        }
    }
}
