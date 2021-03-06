package cn.rpc.common.annotation;

import java.lang.annotation.*;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/4 23:26
 * @Author: 阿左不是蜗牛
 * @Description: 消息协议注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageProtocolAno {
    String value() default "";
}
