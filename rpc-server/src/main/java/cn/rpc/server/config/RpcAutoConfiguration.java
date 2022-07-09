package cn.rpc.server.config;

import cn.rpc.client.discovery.impl.ZooKeeperServiceDiscovery;
import cn.rpc.client.loadbalance.LoadBalance;
import cn.rpc.client.net.impl.NettyNetClient;
import cn.rpc.client.proxy.ClientProxyFactory;
import cn.rpc.common.annotation.LoadBalanceAno;
import cn.rpc.common.annotation.MessageProtocolAno;
import cn.rpc.common.exception.RpcException;
import cn.rpc.common.protocol.MessageProtocol;
import cn.rpc.server.RpcServer;
import cn.rpc.server.handler.ServerRequestHandler;
import cn.rpc.server.net.NettyRpcServer;
import cn.rpc.server.properties.RpcConfig;
import cn.rpc.server.register.ServiceRegister;
import cn.rpc.server.register.impl.ZookeeperServiceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.*;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/4 22:38
 * @Author: 阿左不是蜗牛
 * @Description: RPC自动配置类，注入需要的bean
 */
@Configuration
@EnableConfigurationProperties(RpcConfig.class)
public class RpcAutoConfiguration {

    @Bean
    public RpcConfig rpcConfig(){
        return new RpcConfig();
    }

    /**
     * 服务注册bean对象
     * @param rpcConfig
     * @return
     */
    @Bean
    public ServiceRegister serviceRegister(@Autowired RpcConfig rpcConfig){

        return new ZookeeperServiceRegister(
                rpcConfig.getRegisterAddress(),
                rpcConfig.getServerPort(),
                rpcConfig.getProtocol(),
                rpcConfig.getWeight()
        );
    }

    /**
     * 服务端请求处理器bean对象
     * @param serviceRegister
     * @param rpcConfig
     * @return
     */
    @Bean
    public ServerRequestHandler requestHandler(@Autowired ServiceRegister serviceRegister, @Autowired RpcConfig rpcConfig){
        return new ServerRequestHandler(getMessageProtocol(rpcConfig.getProtocol()) ,serviceRegister);

    }

    /**
     * RpcServer
     * @param requestHandler
     * @param rpcConfig
     * @return
     */
    @Bean
    public RpcServer rpcServer(@Autowired ServerRequestHandler requestHandler, @Autowired RpcConfig rpcConfig){

        return new NettyRpcServer(rpcConfig.getServerPort(), rpcConfig.getProtocol(), requestHandler);

    }

    /**
     * 代理工厂bean
     * @param rpcConfig
     * @return
     */
    @Bean
    public ClientProxyFactory proxyFactory(@Autowired RpcConfig rpcConfig){
        ClientProxyFactory clientProxyFactory = new ClientProxyFactory();
        // 设置服务发现者
        clientProxyFactory.setServiceDiscovery(new ZooKeeperServiceDiscovery(rpcConfig.getRegisterAddress()));

        // 设置支持的协议
        Map<String, MessageProtocol> supportMessageProtocols = buildSupportMessageProtocols();

        clientProxyFactory.setSupportMessageProtocols(supportMessageProtocols);
        // 设置负载均衡算法
        LoadBalance loadBalance = getLoadBalance(rpcConfig.getLoadBalance());
        clientProxyFactory.setLoadBalance(loadBalance);
        // 设置网络层实现
        clientProxyFactory.setNetClient( new NettyNetClient());

        return clientProxyFactory;
    }



    /**
     * 利用JDK的SPI机制
     * @param name
     * @return
     */
    private LoadBalance getLoadBalance(String name) {
        ServiceLoader<LoadBalance> loadBalances = ServiceLoader.load(LoadBalance.class);
        Iterator<LoadBalance> iterator = loadBalances.iterator();
        while (iterator.hasNext()){
            LoadBalance loadBalance = iterator.next();
            LoadBalanceAno annotation = loadBalance.getClass().getAnnotation(LoadBalanceAno.class);
            Assert.notNull(annotation, "load balance name can not be empty!");
            if (name.equals(annotation.value())) {
                return loadBalance;
            }
        }
        throw new RpcException("invalid message loadBalance config!");
    }

    /**
     * 创建支持的协议
     * @return
     */
    private Map<String, MessageProtocol> buildSupportMessageProtocols() {
        Map<String, MessageProtocol> supportMessageProtocols = new HashMap<>();
        ServiceLoader<MessageProtocol> loader = ServiceLoader.load(MessageProtocol.class);
        Iterator<MessageProtocol> iterator = loader.iterator();
        while (iterator.hasNext()) {
            MessageProtocol messageProtocol = iterator.next();
            MessageProtocolAno ano = messageProtocol.getClass().getAnnotation(MessageProtocolAno.class);
            Assert.notNull(ano, "message protocol name can not be empty!");
            supportMessageProtocols.put(ano.value(), messageProtocol);
        }
        return supportMessageProtocols;
    }

    /**
     * 根据协议名称，获取协议对象
     * @param protocol
     * @return
     */
    private MessageProtocol getMessageProtocol(String protocol) {
        // 利用JDK的SPI机制
        ServiceLoader<MessageProtocol> protocols = ServiceLoader.load(MessageProtocol.class);
        Iterator<MessageProtocol> iterator = protocols.iterator();
        while (iterator.hasNext()){
            MessageProtocol messageProtocol = iterator.next();
            MessageProtocolAno messageProtocolAno = messageProtocol.getClass().getAnnotation(MessageProtocolAno.class);
            Assert.notNull(messageProtocolAno, "message protocol name can not be empty!");
            if (messageProtocolAno.value().equals(protocol)) {
                return messageProtocol;
            }
        }
        throw new RpcException("invalid message protocol config!");

    }

}
