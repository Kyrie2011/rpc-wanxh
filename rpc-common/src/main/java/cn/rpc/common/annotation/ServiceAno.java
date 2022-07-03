package cn.rpc.common.annotation;

import java.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 21:04
 * @Author: 阿左不是蜗牛
 * @Description: 被该注解标记的服务了提供远程RPC访问功能
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ServiceAno {
}
