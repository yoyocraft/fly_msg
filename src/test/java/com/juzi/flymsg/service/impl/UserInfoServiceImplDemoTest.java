package com.juzi.flymsg.service.impl;

import com.juzi.flymsg.model.dto.UserRegistryRequest;
import com.juzi.flymsg.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author codejuzi
 * @CreateTime 2023/4/2
 */
@SpringBootTest
class UserInfoServiceImplDemoTest {

    @Resource
    private UserInfoService userInfoService;

    @Test
    void userRegistry() {
        String userAccount = "codejuzi5";
        String userPassword = "12345678";
        String checkedPassword = "12345678";
        // 封装请求
        UserRegistryRequest userRegistryRequest = new UserRegistryRequest();
        userRegistryRequest.setUserAccount(userAccount);
        userRegistryRequest.setUserPassword(userPassword);
        userRegistryRequest.setCheckedPassword(checkedPassword);

        Long userId = userInfoService.userRegistry(userRegistryRequest);
        System.out.println("userId = " + userId);
    }
}