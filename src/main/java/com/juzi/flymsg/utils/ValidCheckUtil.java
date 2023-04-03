package com.juzi.flymsg.utils;

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

    public static void commonLoginRegistryCheck(String userAccount, String userPassword) {
        // 1、校验
//        a. 账号、密码、校验密码非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new RuntimeException("参数异常");
        }
//        b. 账号长度 < 10
        userAccount = userAccount.trim();
        if (userAccount.length() > USER_ACCOUNT_MAX_LEN) {
            throw new RuntimeException("账号过长");
        }
//        c. 密码长度 >= 6
        if (userPassword.length() < USER_PASSWORD_MIN_LEN) {
            throw new RuntimeException("密码过短");
        }
//        f. 账号不能包含特殊符号
        String validPattern = "^[a-zA-Z0-9]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            throw new RuntimeException("账号包含特殊字符");
        }
    }

    public static void registryCheck(String userAccount, String userPassword, String checkedPassword) {
        // 1、校验
        commonLoginRegistryCheck(userAccount, userPassword);
//        a. 账号、密码、校验密码非空
        if (StringUtils.isBlank(checkedPassword)) {
            throw new RuntimeException("参数异常");
        }
//        d. 校验密码长度 >= 6
        if (checkedPassword.length() < USER_PASSWORD_MIN_LEN) {
            throw new RuntimeException("密码过短");
        }
//        e. 密码 == 校验密码
        if (!userPassword.equals(checkedPassword)) {
            throw new RuntimeException("两次输入密码不一致");
        }
    }
}
