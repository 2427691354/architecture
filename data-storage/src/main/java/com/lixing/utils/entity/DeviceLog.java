package com.lixing.utils.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author cc
 * @date 2020/06/10
 **/
@Data
@Document(collection = "devicelog")
public class DeviceLog {

    String gate_id;
    Date timeStamp;
    Integer status;

}
