package com.juzi.flymsg.model.dto;

import lombok.Data;

/**
 * 用户注册的请求对象
 *
 * @author codejuzi
 * @CreateTime 2023/4/3
 */
@Data
public class UserRegistryRequest {

    private String userAccount;

    private String userPassword;

    private String checkedPassword;
}


