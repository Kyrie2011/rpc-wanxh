package cn.rpc.common.serializer;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.nio.charset.StandardCharsets;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/1 0:26
 * @Author: 阿左不是蜗牛
 * @Description: ZK序列化器(节点数据序列化)
 */
public class ZooKeeperSerializer implements ZkSerializer {
    /**
     * ZK节点序列化
     * @param o
     * @return
     * @throws ZkMarshallingError
     */
    @Override
    public byte[] serialize(Object o) throws ZkMarshallingError {
        return String.valueOf(o).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 反序列化
     * @param bytes
     * @return
     * @throws ZkMarshallingError
     */
    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
