package com.lixing.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Set;

@Data
@ApiModel(description = "权限")
public class Permission {

    private String id;
    /**
     * 资源名称
     */
    @ApiModelProperty(value = "权限名称")
    private String name;
    /**
     * 资源类型 BUTTON MENU
     */
    @ApiModelProperty(value = "权限类型")
    private String type;

    /**
     * 资源地址
     */
    @ApiModelProperty(value = "权限地址")
    private String url;

    /**
     * 是否删除0否1是,逻辑删除
     */
    //@ApiModelProperty(value = "是否删除0否1是")
    private Boolean available=Boolean.FALSE;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sorted;


    private Date createdTime;

}