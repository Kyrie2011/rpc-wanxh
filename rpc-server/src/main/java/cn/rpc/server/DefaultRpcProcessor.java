package cn.rpc.server;

import cn.rpc.client.proxy.ClientProxyFactory;
import cn.rpc.common.annotation.ServiceAno;
import cn.rpc.common.model.Service;
import cn.rpc.server.register.ServiceObject;
import cn.rpc.server.register.ServiceRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/9 22:53
 * @Author: 阿左不是蜗牛
 * @Description: RPC处理者，支持服务启动暴露，自动注入Service
 */
public class DefaultRpcProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(DefaultRpcProcessor.class);

    private ClientProxyFactory clientProxyFactory;

    private ServiceRegister serviceRegister;

    private RpcServer rpcServer;


    public DefaultRpcProcessor(ClientProxyFactory clientProxyFactory, ServiceRegister serviceRegister, RpcServer server){
       this.clientProxyFactory = clientProxyFactory;
       this.serviceRegister = serviceRegister;
       this.rpcServer = server;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // Spring 启动完毕过后会收到一个事件通知
        if (Objects.isNull(contextRefreshedEvent.getApplicationContext().getParent())) {
            ApplicationContext context = contextRefreshedEvent.getApplicationContext();
            // 开启服务
            startService(context);

            // 注入服务
            injectService(context);
        }


    }

    private void startService(ApplicationContext context) {
        Map<String, Object> beans = context.getBeansWithAnnotation(ServiceAno.class);
        if (!CollectionUtils.isEmpty(beans)) {
            boolean startServiceFlag = true;
            for (Object obj : beans.values()) {
                try{
                    Class<?> clazz = obj.getClass();
                    Class<?>[] interfaces = clazz.getInterfaces();
                    ServiceObject serviceObj = null;

                    /**
                     * 如果只实现了一个接口就用父类的className作为服务名
                     * 如果该类实现了多个接口，则用注解里的value作为服务名
                     */
                    if (interfaces.length != 1) {
                        ServiceAno annotation = clazz.getAnnotation(ServiceAno.class);
                        String value = annotation.value();
                        if (value.equals("")){
                            startServiceFlag = false;
                            throw new UnsupportedOperationException("The exposed interface is not specific with '" + obj.getClass().getName() + "'");

                        }
                        serviceObj = new ServiceObject(value, Class.forName(value), obj);
                    }else {
                        Class<?> supperClass = interfaces[0];
                        serviceObj = new ServiceObject(supperClass.getName(), supperClass, obj);
                    }

                    serviceRegister.register(serviceObj);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            if (startServiceFlag) {
                // 启动服务端NettyServer
                rpcServer.start();
            }

        }
    }

    private void injectService(ApplicationContext context) {
    }
}
