package com.juzi.flymsg.service;

import com.juzi.flymsg.model.dto.UserRegistryRequest;
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
     * @param userRegistryRequest 用户注册请求对象信息
     * @return 用户id
     */
    Long userRegistry(UserRegistryRequest userRegistryRequest);
}
