package com.juzi.flymsg.utils;

import com.juzi.flymsg.common.ErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.juzi.flymsg.constant.UserConstant.USER_ACCOUNT_MAX_LEN;
import static com.juzi.flymsg.constant.UserConstant.USER_PASSWORD_MIN_LEN;

/**
 * 合法性校验工具类
 *
 * @author codejuzi
 * @CreateTime 2023/4/3
 */
public class ValidCheckUtil {

    /**
     * 登录和注册同样的校验
     *
     * @param userAccount  user account inputted
     * @param userPassword user password inputted
     */
    public static void commonLoginRegistryCheck(String userAccount, String userPassword) {
        // 1、校验
        // a. 账号、密码、校验密码非空
        ThrowUtil.throwIf(StringUtils.isAnyBlank(userAccount, userPassword), ErrorCode.PARAM_ERROR);
        // b. 账号长度 < 10
        userAccount = userAccount.trim();
        ThrowUtil.throwIf(userAccount.length() > USER_ACCOUNT_MAX_LEN, ErrorCode.PARAM_ERROR, "账号过长！");
        // c. 密码长度 >= 6
        ThrowUtil.throwIf(userPassword.length() < USER_PASSWORD_MIN_LEN, ErrorCode.PARAM_ERROR, "密码过短！");
        // f. 账号不能包含特殊符号
        String validPattern = "^[a-zA-Z0-9]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        ThrowUtil.throwIf(!matcher.find(), ErrorCode.PARAM_ERROR, "账号包含特殊字符！");
    }

    /**
     * 注册特有的校验
     *
     * @param userAccount     user account inputted
     * @param userPassword    user password inputted
     * @param checkedPassword checked password inputted
     */
    public static void registryCheck(String userAccount, String userPassword, String checkedPassword) {
        // 1、校验
        commonLoginRegistryCheck(userAccount, userPassword);
        // a. 账号、密码、校验密码非空
        ThrowUtil.throwIf(StringUtils.isBlank(checkedPassword), ErrorCode.PARAM_ERROR);
        // d. 校验密码长度 >= 6
        ThrowUtil.throwIf(checkedPassword.length() < USER_PASSWORD_MIN_LEN, ErrorCode.PARAM_ERROR, "密码过短！");
        // e. 密码 == 校验密码
        ThrowUtil.throwIf(!userPassword.equals(checkedPassword), ErrorCode.PARAM_ERROR, "两次输入密码不一致");
    }
}
