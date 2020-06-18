package com.lixing.util;
import com.mongodb.client.result.UpdateResult;
import net.sf.json.JSONObject;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@PropertySource(value = "classpath:packettype.properties") // 数据解析规则配置
public class DataParsingUtil {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    Environment env;

    static  DataParsingUtil dataParsingUtil;

    @PostConstruct
    void init(){
        dataParsingUtil = this;
    }

    //默认头部33位（0，18）
    public static final int position=18;
    //时间
    public static final int timedata=6;

    public static void parse(byte[] receive,Producer<String, String> producer) throws ParseException {
        //   分析数据开始
        Map<String, Object> resultMap = new HashMap<>();

        int positoin = 18; // 不需要解密的数据位数
        byte[] head = new byte[positoin];
        for (int j = 0; j < positoin; j++) {// 获取不需要解密的数据
            head[j] = receive[j];
        }

        //公司标识符
        String comID = new String(head).substring(2, 6);

        //车间ID
        String GateID = new String(head).substring(6, 18);

//        dataParsingUtil.observe.add(GateID);
//
//        System.err.println(dataParsingUtil.observe);
//        if(dataParsingUtil.observe!=null){
//            Update update2 = new Update();
//            update2.set("status",0);
//            dataParsingUtil.mongoTemplate.updateMulti(new Query(), update2, WorkShop.class);
//
//            Query query = new Query();
//            query.addCriteria(Criteria.where("gate_id").in(dataParsingUtil.observe2));
//
//            Update update = new Update();
//            update.set("status",1);
//            dataParsingUtil.mongoTemplate.updateMulti(query, update, WorkShop.class);
//        }




        byte[] encode = new byte[receive.length - positoin];
        for (int j = positoin; j < receive.length; j++) {
            encode[j - positoin] = receive[j];
        }

        String time = encode[0] + 1900 + "-" + (encode[1] + 1) + "-"
                + UtilTools.addZero(Integer.toString((int) (encode[2]))) + " "
                + UtilTools.addZero(Integer.toString((int) (encode[3]) + 8)) + ":"
                + UtilTools.addZero(Integer.toString((int) (encode[4]))) + ":"
                + UtilTools.addZero(Integer.toString((int) (encode[5])));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        System.err.println(date);
        long ts = date.getTime();
        // 每个数据其实字节的位数
        String[] indexs = {};
        // 每个数据目标的byte长度
        String[] bytes = {};

        // 每个数据转换格式，
        // 0代表不要转换，
        // 1代表只需转成10进制，
        // 2代表转成10进制后再乘以一个系数，
        // 3代表转成10进制后再减去一个常数，
        // 4代表转成10进制后减去一个常数在乘以一个系数
        String[] sixteenToTen = {};
        // 每个数据的名称
        String[] natures = {};
        // 每个数据转换中需要的系数
        String[] coefficient = {};
        String bigNature = "";
        //判断得到的包序数，为每个包生成indexs、bytes、sixteenToTen、natures、coefficient

        switch (encode[6]) {
            case 1:
                indexs = dataParsingUtil.env.getProperty("environment_index").split(",");
                bytes = dataParsingUtil.env.getProperty("environment_byte").split(",");
                sixteenToTen = dataParsingUtil.env.getProperty("environment_sixteen").split(",");
                coefficient = dataParsingUtil.env.getProperty("environment_coefficient").split(",");
                natures = dataParsingUtil.env.getProperty("environment").split(",");
                break;
            case 2:
                indexs = dataParsingUtil.env.getProperty("equipment_index").split(",");
                bytes = dataParsingUtil.env.getProperty("equipment_byte").split(",");
                sixteenToTen = dataParsingUtil.env.getProperty("equipment_sixteen").split(",");
                coefficient = dataParsingUtil.env.getProperty("equipment_coefficient").split(",");
                natures = dataParsingUtil.env.getProperty("equipment").split(",");
                break;
            default:
                break;
        }

        // 通过indexs和bytes读取目标数据并返回结果
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < indexs.length; i++) {
            String figure = "";
            for (int j = 0; j < Integer.parseInt(bytes[i]); j++) {
                int dataIndex = Integer.parseInt(indexs[i]) + Integer.parseInt(bytes[i]) - j - 1;
                if (dataIndex < encode.length) {
                    byte number = encode[Integer.parseInt(indexs[i]) + Integer.parseInt(bytes[i]) - j - 1];
                    figure = figure + UtilTools.addZero(Integer.toHexString(UtilTools.byte2Int(number)));
                    figure = UtilTools.removeZero(figure);
                } else {
                    break;
                }
            }

            if (figure.length() > 0 && figure.length() < 8) {
                if (sixteenToTen[i].equals("0"))
                    map.put(natures[i], figure);
                else if (sixteenToTen[i].equals("1"))
                    map.put(natures[i], Integer.parseInt(figure, 16));
                else if (sixteenToTen[i].equals("2"))
                    map.put(natures[i], Integer.parseInt(figure, 16) * Double.parseDouble(coefficient[i].split("/")[0])/Integer.parseInt(coefficient[i].split("/")[1]));
                  //  System.err.println("2------"+Integer.parseInt(figure, 16)*1.0/10);
                else if (sixteenToTen[i].equals("3"))
                    map.put(natures[i], Integer.parseInt(figure, 16) - Integer.parseInt(coefficient[i]));
                else if (sixteenToTen[i].equals("4"))
                    map.put(natures[i], (Integer.parseInt(figure, 16) - Integer.parseInt(coefficient[i].split("/")[0]))
                            * Double.parseDouble(coefficient[i].split("/")[1]));
            } else if (figure.length() >= 8) {
                if (sixteenToTen[i].equals("0"))
                    map.put(natures[i], figure);
                else if (sixteenToTen[i].equals("1"))
                    map.put(natures[i], Long.parseLong(figure, 16));
                else if (sixteenToTen[i].equals("2"))
                    map.put(natures[i], Long.parseLong(figure, 16) * Double.parseDouble(coefficient[i]));
                else if (sixteenToTen[i].equals("3"))
                    map.put(natures[i], Long.parseLong(figure, 16) - Integer.parseInt(coefficient[i]));
                else if (sixteenToTen[i].equals("4"))
                    map.put(natures[i], (Long.parseLong(figure, 16) - Integer.parseInt(coefficient[i].split("/")[0]))
                            * Double.parseDouble(coefficient[i].split("/")[1]));
            } else
                map.put(natures[i], figure);
        }

        Map<String, Object> headMap = new HashMap<String, Object>();
        headMap.put("typeNO", encode[6]);
        headMap.put("GateID", GateID);

        resultMap.put("timeStamp", ts);
        resultMap.put("head",headMap);
        resultMap.put("body",map);


        System.out.println("开始存"+JSONObject.fromObject(resultMap).toString());
        //TODO 生产到kafka
        producer.send(new ProducerRecord<String, String>("first",  JSONObject.fromObject(resultMap).toString()));
//        producer.send(new KeyedMessage<String, String>(configureProperties.getProperty("topic"), null, JSONObject.fromObject(resultMap).toString()));
        System.out.println("结束存"+JSONObject.fromObject(resultMap).toString());
    }

}
