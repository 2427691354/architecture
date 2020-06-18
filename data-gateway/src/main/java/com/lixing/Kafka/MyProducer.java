package com.lixing.Kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

/**
 * @author cc
 * @date 2020/05/14
 **/
public class MyProducer {
    static Properties props = new Properties();

    //初始化kafka连接参数
    static {
        // Kafka服务端的主机名和端口号
//        props.put("bootstrap.servers", "iZuf65r37552x8xpeigpx9Z:9092,iZuf65vg2nr3x1bwi39rjwZ:9092,iZuf6b7o9hu1q97s6isvkgZ:9092");
        props.put("bootstrap.servers", "iZuf6b7o9hu1q97s6isvkgZ:9092");
        // 等待所有副本节点的应答
        props.put("acks", "all");
        // 消息发送最大尝试次数
        props.put("retries", 0);
        // 一批消息处理大小
        props.put("batch.size", 16384);
        // 请求延时
        props.put("linger.ms", 1);
        // 发送缓存区内存大小
        props.put("buffer.memory", 33554432);
        // key序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }

    public static Producer<String, String> init() {
        Producer<String, String> producer = new KafkaProducer<>(props);
        return producer;
    }
}
