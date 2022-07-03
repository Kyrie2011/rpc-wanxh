package cn.rpc.common.util;


import java.util.function.Function;

import static cn.rpc.common.util.GenericService.GENERIC;

/**
 * 处理泛型的方法选项
 *
 * @param <T>
 */
public class GenericMethodOption<T> extends MethodOption.NameKeyOption<T> {

    /**
     * 构造函数
     *
     * @param clazz
     * @param className
     * @param nameFunction
     */
    public GenericMethodOption(final Class clazz, final String className, final Function<String, T> nameFunction) {
        super(GENERIC.test(clazz) ? null : clazz, className, nameFunction);
    }
}
