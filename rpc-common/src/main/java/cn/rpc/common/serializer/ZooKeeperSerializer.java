package cn.rpc.common.serializer;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

/**
 * @program: rpc-wanxh
 * @Date: 2022/7/1 0:26
 * @Author: 阿左不是蜗牛
 * @Description: ZK序列化器(节点数据序列化)
 */
public class ZooKeeperSerializer implements ZkSerializer {
    @Override
    public byte[] serialize(Object o) throws ZkMarshallingError {
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return null;
    }
}
