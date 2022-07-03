package cn.rpc.common.exception;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/3 17:40
 * @Author: 阿左不是蜗牛
 * @Description: 代理包装异常
 */
public class ProxyException  extends  LafException{
    private static final long serialVersionUID = 7513510706505625106L;

    public ProxyException() {
        super(null, null, false, false, null, false);
    }

    public ProxyException(String message) {
        super(message, null, false, false, null, false);
    }

    public ProxyException(String message, Throwable cause) {
        super(message, cause, false, false, null, false);
    }

}
