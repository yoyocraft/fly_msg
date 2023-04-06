package com.juzi.flymsg.service.impl;

import com.juzi.flymsg.model.dto.UserRegistryRequest;
import com.juzi.flymsg.model.vo.UserVO;
import com.juzi.flymsg.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author WS
 * @CreateTime 22:06
 */
@SpringBootTest
class UserInfoServiceImplTest {

    @Resource
    private UserInfoService userInfoService;

    @Test
    void userRegistry() {
        UserRegistryRequest userRegistryRequest = new UserRegistryRequest();
        userRegistryRequest.setUserAccount("ws2");
        userRegistryRequest.setUserPassword("12345678");
        userRegistryRequest.setCheckedPassword("12345678");
        Long userId = userInfoService.userRegistry(userRegistryRequest);
        assert userId != null && userId > 0;
        System.out.println("userId = " + userId);
    }

    @Test
    void userSelectById() {
        long userId = 1;
        UserVO userVO = userInfoService.userSelectById(userId);
        assert userVO != null;
        System.out.println("userVO = " + userVO);
    }
}