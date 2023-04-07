package com.juzi.flymsg.model.dto.user;

import lombok.Data;

/**
 * 用户登录请求对象
 * @author codejuzi
 * @CreateTime 2023/4/3
 */
@Data
public class UserLoginRequest {

    private String userAccount;

    private String userPassword;
}
