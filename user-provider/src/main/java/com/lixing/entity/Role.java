package com.lixing.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;


@Data
@Document(collection = "role")
public class Role {
    private String name;

    @Field("permissions")
    private Set<Permission> permissions;
}