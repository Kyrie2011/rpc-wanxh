package cn.rpc.common.constant;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/2 14:21
 * @Author: 阿左不是蜗牛
 * @Description: 常量
 */
public class RpcConstant {

    private RpcConstant(){}

    /**
     * ZooKeeper服务注册地址
     */
    public static final String ZK_SERVICE_PATH = "/rpc";

    /**
     * 路径分隔符号
     */
    private static final String PATH_DELIMITER = "/";

    //-----------------------序列化协议------------------------//
    /**
     * java序列化协议
     */
    public static final String PROTOCOL_JAVA = "java";

    /**
     * protobuf序列化协议
     */
    public static final String PROTICOL_PROTOBUF = "protobuf";


    //-----------------------负载均衡策略------------------------//
    /**
     * 随机
     */
    public static final String BALANCE_RANDOM = "random";

    /**
     * 轮询
     */
    public static final String BALANCE_ROUND = "round";

    /**
     * 加权轮询
     */
    public static final String BALANCE_WEIGHT_ROUND = "weightRound";

    /**
     * 平滑加权轮询
     */
    public static final String BALANCE_SMOOTH_WEIGHT_ROUND = "smoothWeightRound";



}
