package com.juzi.flymsg.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息VO类
 *
 * @author codejuzi
 * @CreateTime 2023/4/3
 */
@Data
public class UserInfoVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 账号
     */
    private String userAccount;

    private static final long serialVersionUID = 1L;
}
