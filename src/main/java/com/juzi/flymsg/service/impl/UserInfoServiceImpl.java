package com.juzi.flymsg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.flymsg.mapper.UserLoginInfoMapper;
import com.juzi.flymsg.model.dto.UserRegistryRequest;
import com.juzi.flymsg.model.entity.UserInfo;
import com.juzi.flymsg.model.entity.UserLoginInfo;
import com.juzi.flymsg.service.UserInfoService;
import com.juzi.flymsg.mapper.UserInfoMapper;
import com.juzi.flymsg.utils.ValidCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author codejuzi
 * @description 针对表【userInfo(用户信息表)】的数据库操作Service实现
 * @createDate 2023-04-02 20:25:45
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {


    /**
     * 盐值
     */
    private static final String SALT = "fly_msg";

    @Resource
    private UserLoginInfoMapper userLoginInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long userRegistry(UserRegistryRequest userRegistryRequest) {
        String userAccount = userRegistryRequest.getUserAccount();
        String userPassword = userRegistryRequest.getUserPassword();
        String checkedPassword = userRegistryRequest.getCheckedPassword();
        // 1、校验
        ValidCheckUtil.registryCheck(userAccount, userPassword, checkedPassword);
//        g. 账号不能重复 => 查数据库
        // select id, userAccount, userPassword from userLoginInfo where userAccount = 'user1';
        LambdaQueryWrapper<UserLoginInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLoginInfo::getUserAccount, userAccount);
        UserLoginInfo userLoginInfo = userLoginInfoMapper.selectOne(queryWrapper);
        if(userLoginInfo != null) {
            throw new RuntimeException("账号已经存在");
        }

        //2、加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3、插入数据库
        UserInfo userInfo = new UserInfo();
        userInfo.setUserAccount(userAccount);
        userInfo.setUserPassword(encryptPassword);
        this.save(userInfo);

        UserLoginInfo loginInfo = new UserLoginInfo();
        loginInfo.setUserAccount(userAccount);
        loginInfo.setUserPassword(encryptPassword);
        loginInfo.setUserId(userInfo.getId());
        userLoginInfoMapper.insert(loginInfo);

        return userInfo.getId();
    }
}




