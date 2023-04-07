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
     * if condition, throw
     *
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        if(condition) {
            throw new BusinessException(errorCode);
        }
    }

    /**
     * if condition, throw
     *
     * @param condition   条件
     * @param errorCode   错误码
     * @param description 描述
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String description) {
        if(condition) {
            throw new BusinessException(errorCode, description);
        }
    }
}
