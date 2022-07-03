package cn.rpc.common.util;

/**
 * 三元组函数
 *
 * @param <T>
 * @param <U>
 * @param <S>
 * @param <R>
 */
public interface TriFunction<T, U, S, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param s the third function argument
     * @return the function result
     */
    R apply(T t, U u, S s);
}
