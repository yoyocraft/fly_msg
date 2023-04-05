package com.juzi.flymsg.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回响应体
 *
 * @author codejuzi
 * @CreateTime 2023/4/5
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -8023942560208172994L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 数据
     */
    private T data;

    /**
     * 响应的简要信息
     */
    private String message;

    /**
     * 响应的详细信息
     */
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
