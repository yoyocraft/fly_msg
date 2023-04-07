package com.juzi.flymsg.constant;

/**
 * 用户常量
 *
 * @author codejuzi
 * @CreateTime 2023/4/3
 */
public interface UserConstant {

    // region login registry
    /**
     * 盐值
     */
    String SALT = "codejuzi";

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    /**
     * 用户账号的最大长度
     */
    int USER_ACCOUNT_MAX_LEN = 10;

    /**
     * 用户密码的最小长度
     */
    int USER_PASSWORD_MIN_LEN = 6;
    // endregion

    // region auth

    /**
     * 管理员
     */
    int ADMIN_ROLE = 1;

    // endregion
}
