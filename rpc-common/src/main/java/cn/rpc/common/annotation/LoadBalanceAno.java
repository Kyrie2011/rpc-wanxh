package cn.rpc.common.annotation;

import java.lang.annotation.*;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/9 0:28
 * @Author: 阿左不是蜗牛
 * @Description: 负载均衡注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoadBalanceAno {

    String value() default "";
}
