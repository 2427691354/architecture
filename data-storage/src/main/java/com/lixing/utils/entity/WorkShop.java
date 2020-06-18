package com.lixing.utils.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

/**
 * @author cc
 * @date 2020/06/10
 **/
@Data
@Document(collection = "workshop")
public class WorkShop {
    private String gate_id;
    Integer status;
}
