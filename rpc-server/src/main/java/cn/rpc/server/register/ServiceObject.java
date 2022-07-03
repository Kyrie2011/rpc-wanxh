package cn.rpc.server.register;

import lombok.Data;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 21:37
 * @Author: 阿左不是蜗牛
 * @Description: 服务持有对象，保存具体的服务信息备用
 */
@Data
public class ServiceObject {

    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务的Class对象
     */
    private Class<?> clazz;

    /**
     * 服务实例
     */
    private Object obj;
}
