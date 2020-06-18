package com.lixing;

import com.lixing.Kafka.MyProducer;
import com.lixing.Server.UDPServer;
import com.lixing.util.WorkShop;
import com.lixing.util.WorkShopData;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author cc
 * @date 2020/06/09
 **/
@Component
@SpringBootApplication
public class Application {

    @Value("${server.port}")
    private   Integer PORT;

    @Autowired
    MongoTemplate mongoTemplate;

    private static Application application;

    static List<WorkShop> workShops = new ArrayList<>();

    @PostConstruct
    void init(){
        application = this;

        Query query = new Query();
        query.fields().exclude("_id");//不包含该字段
        query.fields().include("gate_id");
        workShops = application.mongoTemplate.findAll(WorkShop.class);
    }

    public static void main(String[] args) throws Exception {


        timer();
        SpringApplication.run(Application.class, args);
        UDPServer.run(application.PORT,MyProducer.init());
    }

    // 5分钟一次：查看各车间1分钟内有无上报数据 ，若有，为上线；若无，为离线
    public static void timer() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setCalendar(new GregorianCalendar(
                new SimpleTimeZone(0, "GMT")));
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Calendar nowTime = Calendar.getInstance();
                Calendar nowTime2 = Calendar.getInstance();
//                nowTime.add(Calendar.HOUR, -8);
                nowTime.add(Calendar.MINUTE, -1);

                if(workShops.size()>0){
                    for(WorkShop workShop :workShops){
                        Query query = new Query();
                        query.addCriteria(Criteria.where("head.GateID").is(workShop.getGate_id()))
                                .addCriteria(Criteria.where("timeStamp").gte(nowTime.getTime().getTime()));
                        List<WorkShopData> workShopData = application.mongoTemplate.find(query, WorkShopData.class);
                        if(workShopData.size()>0){
                            System.err.println(workShop.getGate_id()+"上线");
                        }
                        else{
                            System.err.println(workShop.getGate_id()+"离线");
                        }
                    }

                }

            }

        }, c.getTime(), 1000*60*5);
    }
}
