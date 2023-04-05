package com.juzi.flymsg.model.vo;

import lombok.Data;

/**
 * 查询用户返回的VO
 *
 * @author codejuzi
 * @CreateTime 2023/4/5
 */
@Data
public class UserVO {
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
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：0 - user, 1 - admin
     */
    private Integer userRole;
}
