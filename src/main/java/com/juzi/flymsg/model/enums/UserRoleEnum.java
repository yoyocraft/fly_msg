package com.juzi.flymsg.model.enums;

import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.constant.UserConstant;
import com.juzi.flymsg.utils.ThrowUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色常量类
 *
 * @author codejuzi
 * @CreateTime 2023/4/9
 */
public enum UserRoleEnum {
    /**
     * 管理员
     */
    ADMIN("管理员", UserConstant.ADMIN_ROLE),

    /**
     * 普通用户
     */
    USER("用户", UserConstant.DEFAULT_ROLE),

    /**
     * 被封号的用户
     */
    BAN("被封号", UserConstant.BANNED_ROLE);

    private final String text;

    private final int value;

    UserRoleEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }


    /**
     * 获取值列表
     *
     * @return 值列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据value获取枚举值
     *
     * @param value value
     * @return value对应的枚举值
     */
    public static UserRoleEnum getEnumByValue(int value) {
        ThrowUtil.throwIf(value < 0, ErrorCode.PARAM_ERROR);
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if(value == userRoleEnum.value) {
                return userRoleEnum;
            }
        }
        return null;
    }
}
