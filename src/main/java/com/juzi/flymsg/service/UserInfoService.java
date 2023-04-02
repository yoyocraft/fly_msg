package com.juzi.flymsg.service;

import com.juzi.flymsg.model.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author codejuzi
 * @description 针对表【userInfo(用户信息表)】的数据库操作Service
 * @createDate 2023-04-02 20:25:45
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户注册
     *
     * @param userAccount     用户账号
     * @param userPassword    用户密码
     * @param checkedPassword 校验密码
     * @return 用户id
     */
    Long userRegistry(String userAccount, String userPassword, String checkedPassword);
}
