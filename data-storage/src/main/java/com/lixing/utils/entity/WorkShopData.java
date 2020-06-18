package com.lixing.utils.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

/**
 * @author cc
 * @date 2020/06/09
 **/
@Data
@Document(collection = "workshopdata")
public class WorkShopData {
    private Long timeStamp;
    private Head head;
    private Map<String,Object> body;

    @Data
    public static class Head{
        private String typeNO;
        private String GateID;
    }
}

