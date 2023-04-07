package com.juzi.flymsg.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户修改请求
 *
 * @author codejuzi
 * @CreateTime 2023/4/5
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 2963538264134418108L;

    /**
     * id
     */
    private Long id;

    /**
     * 密码
     */
    private String userPassword;

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

}
