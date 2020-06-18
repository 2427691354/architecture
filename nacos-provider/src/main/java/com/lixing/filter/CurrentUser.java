package com.lixing.filter;

/**
 * @author cc
 * @date 2020/06/04
 **/
import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

}
