package com.juzi.flymsg.model.dto;

import lombok.Data;

/**
 * @author codejuzi
 * @CreateTime 2023/4/5
 */
@Data
public class UserSelectRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户简介
     */
    private String userProfile;

}
