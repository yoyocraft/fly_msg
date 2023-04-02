package com.juzi.flymsg.service.impl;

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
        String userAccount = "codejuzi2";
        String userPassword = "12345678";
        String checkedPassword = "12345678";
        Long userId = userInfoService.userRegistry(userAccount, userPassword, checkedPassword);
        System.out.println("userId = " + userId);
    }
}