package com.juzi.flymsg.common;

/**
 * 异常码枚举类
 *
 * @author codejuzi
 * @CreateTime 2023/4/5
 */
public enum ErrorCode {

    /**
     * 请求响应成功
     */
    SUCCESS(0, "成功", ""),

    /**
     * 参数错误 -> 为空，不符合条件
     */
    PARAM_ERROR(40000, "参数错误", ""),

    /**
     * 系统内部异常
     */
    SYSTEM_ERROR(50000, "系统内部异常", ""),

    /**
     * 未登录
     */
    NO_LOGIN(40100, "未登录", ""),

    /**
     * 无权限
     */
    NO_AUTH(40200, "无权限", "");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 简要信息
     */
    private final String message;

    /**
     * 详细信息
     */
    private final String description;


    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
