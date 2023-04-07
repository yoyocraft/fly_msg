package com.juzi.flymsg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juzi.flymsg.model.entity.UserInfo;
import com.juzi.flymsg.model.dto.user.UserRegistryRequest;
import com.juzi.flymsg.model.vo.UserVO;
import com.juzi.flymsg.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author WS
 * @CreateTime 22:06
 */
@SpringBootTest
class UserInfoServiceImplTest {

    @Resource
    private UserInfoService userInfoService;

    @Test
    void userSelectByName() {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(UserInfo::getUserName, "管理");
        List<UserInfo> userInfoList = userInfoService.list(queryWrapper);
        System.out.println(userInfoList);
    }

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