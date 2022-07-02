package cn.rpc.common.constant;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 15:40
 * @Author: 阿左不是蜗牛
 * @Description: Rpc调用状态枚举/响应码
 */
public enum RpcStatusEnum {
    /**
     * SUCCESS
     */
    SUCCESS(200, "SUCCESS"),
    /**
     * ERROR
     */
    ERROR(500, "ERROR"),
    /**
     * NOT_FOUND
     */
    NOT_FOUND(404,"NOT FOUND");

    private int code;

    private String desc;

    RpcStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
