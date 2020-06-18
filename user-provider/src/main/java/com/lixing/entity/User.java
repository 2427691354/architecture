package com.lixing.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Data
@Document(collection="user")
@ApiModel(description = "用户实体")
public class User{
    /**
     * 用户名
     */
    @Field("user_name")
    @ApiModelProperty(value = "用户名")
    private String userName;
    /**
     * 真实姓名
     */
    @Field("real_name")
    @ApiModelProperty(value = "真实姓名")
    private String realName;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 盐
     */
    private String salt;

    /**
     * 手机号码
     */
    @Field("phone_num")
    @ApiModelProperty(value = "手机号码")
    private String phoneNum;

    /**
     * 是否删除0否1是,逻辑删除
     */
    @ApiModelProperty(value = "是否删除0否1是")
    private Boolean deleted=Boolean.FALSE;


    /**
     * 角色
     */
    @ApiModelProperty(value = "角色")
    @Field("role")
    private Set<Role> roles;

    /**
     * 密码盐. 重新对盐重新进行了定义，用户名+salt，这样就不容易被破解，可以采用多种方式定义加盐
     * @return
     */
    public String getCredentialsSalt(){
        return this.userName+this.salt;
    }
}
