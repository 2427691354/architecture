package com.lixing.utils.Kafka;

import com.alibaba.fastjson.JSON;
import com.lixing.utils.entity.WorkShop;
import com.lixing.utils.entity.WorkShopData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author cc
 * @date 2020/05/15
 **/
@Configuration
public class MyConsumer {

    @Autowired
    MongoTemplate mongoTemplate;

    //1.配置属性值
    static Properties properties = new Properties();
    static {
        //kafka是服务器地址
        properties.put("bootstrap.servers", "iZuf6b7o9hu1q97s6isvkgZ:9092");
        //定义消费者组
        properties.put("group.id", "test-consumer-group");
        //自动提交（offset）
        properties.put("enable.auto.commit", "true");
        //自动处理的间隔时间1秒
        properties.put("auto.commit.interval.ms", "1000");
        //key和values的持久化设置
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }

    public  void consumer() {

        List<WorkShop> word = mongoTemplate.findAll(WorkShop.class);

        System.err.println(word.get(0).getGate_id());
        //2.创建消费者
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        //3.订阅消费topic(可以有多个topic)
//        kafkaConsumer.partitionsFor("testtest3");
        kafkaConsumer.subscribe(Arrays.asList("first"));
        //4.执行消费的操作
        while (true) {
            //100ms消费一次
            //kafkaConsumer.poll(100)读出来，读到records
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);

            for (ConsumerRecord<String, String> record : records) {
                WorkShopData workShopData = JSON.parseObject(record.value(), WorkShopData.class);
                System.err.println(workShopData.getHead());
                mongoTemplate.insert(workShopData);
                //打印偏移量，key，value
                System.err.printf("topic = %s, offset = %d, value = %s \n", record.topic(), record.offset(), record.value());
            }
        }
    }}

