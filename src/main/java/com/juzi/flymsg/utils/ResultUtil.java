package com.juzi.flymsg.utils;

import com.juzi.flymsg.common.BaseResponse;
import com.juzi.flymsg.common.ErrorCode;

/**
 * 返回结果工具类
 *
 * @author codejuzi
 * @CreateTime 2023/4/5
 */
public class ResultUtil {

    /**
     * 成功
     *
     * @param data 成功后的响应结果
     * @param <T>  泛型
     * @return BasesResponse
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), data, "");
    }

    /**
     * 失败
     *
     * @param errorCode 错误码
     * @return
     */
    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse<?> error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

}
