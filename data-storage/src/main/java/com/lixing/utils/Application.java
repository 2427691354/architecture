package com.lixing.utils;

import com.lixing.utils.Kafka.MyConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;

/**
 * @author cc
 * @date 2020/06/09
 **/
@SpringBootApplication
public class Application {

    @Autowired
    MyConsumer consumer;

    static Application application;

    @PostConstruct
    void init(){
        application = this;
    }


    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
        application.consumer.consumer();
    }

}
