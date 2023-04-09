package com.juzi.flymsg.utils;

import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.exception.BusinessException;

/**
 * 异常处理工具类
 *
 * @author codejuzi
 * @CreateTime 2023/4/7
 */
public class ThrowUtil {


    /**
     * 条件成立则抛异常
     *
     * @param condition        条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * if condition, throw
     *
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * if condition, throw
     *
     * @param condition   条件
     * @param errorCode   错误码
     * @param description 描述
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String description) {
        throwIf(condition, new BusinessException(errorCode, description));
    }
}
