package cn.rpc.common.annotation;

import java.lang.annotation.*;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 21:22
 * @Author: 阿左不是蜗牛
 * @Description: 该注解用于标记注入远程服务
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectServiceAno {
}
