package cn.rpc.client.cache;

import cn.rpc.common.model.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 18:49
 * @Author: 阿左不是蜗牛
 * @Description: 服务发现本地缓存
 */
public class ServiceDiscoveryCache {

    /**
     * key : serviceName
     */
    private static final Map<String, List<Service>> SERVICE_MAP = new ConcurrentHashMap<>();

    /**
     * 客户端注入的远程服务的Class名称，service class
     */
    public static final List<String> SERVICE_CLASS_NAMES = new ArrayList<>();

    public static void put(String serviceName, List<Service> serviceList){
        SERVICE_MAP.put(serviceName, serviceList);
    }


    /**
     * 去除指定的值
     * @param serviceName
     * @param service
     */
    public static void remove(String serviceName, Service service){
        SERVICE_MAP.computeIfPresent(serviceName, (key, value) ->
                value.stream().filter(o -> !o.toString().equals(service.toString())).collect(Collectors.toList())
        );
    }

    public static void removeAll(String serviceName) {
        SERVICE_MAP.remove(serviceName);
    }


    public static boolean isEmpty(String serviceName) {
        return SERVICE_MAP.get(serviceName) == null || SERVICE_MAP.get(serviceName).size() == 0;
    }

    public static List<Service> get(String serviceName) {
        return SERVICE_MAP.get(serviceName);
    }

}
