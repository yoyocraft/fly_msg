package com.juzi.flymsg.annotation;

import com.juzi.flymsg.constant.UserConstant;

import java.lang.annotation.*;

/**
 * 权限校验注解
 *
 * @author codejuzi
 * @CreateTime 2023/4/9
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthCheck {

    /**
     * 必须拥有某个角色
     *
     * @return 角色 UserConstant
     */
    int mustRole() default UserConstant.DEFAULT_ROLE;
}
