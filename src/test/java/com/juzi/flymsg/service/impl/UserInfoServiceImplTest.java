package com.juzi.flymsg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juzi.flymsg.model.entity.UserInfo;
import com.juzi.flymsg.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author codejuzi
 * @CreateTime 2023/4/7
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
}