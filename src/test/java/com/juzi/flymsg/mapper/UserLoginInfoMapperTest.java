package com.juzi.flymsg.mapper;

import com.juzi.flymsg.model.entity.UserLoginInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author codejuzi
 * @CreateTime 2023/4/9
 */
@SpringBootTest
class UserLoginInfoMapperTest {

    @Resource
    private UserLoginInfoMapper userLoginInfoMapper;

    @Test
    void isExist() {

        String userAccount = "codejuzi";
        UserLoginInfo userLoginInfo = userLoginInfoMapper.isExist(userAccount);
        assertNotNull(userLoginInfo);
        System.out.println("userLoginInfo = " + userLoginInfo);
    }

    @Test
    void updateUserPasswordBoolean() {
        long userId = 6L;
        boolean updateUserPasswordBoolean = userLoginInfoMapper.updateUserPasswordBoolean(userId, "12345678");
        System.out.println("updateUserPasswordBoolean = " + updateUserPasswordBoolean);
    }
}