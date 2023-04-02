package com.juzi.flymsg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.flymsg.mapper.UserLoginInfoMapper;
import com.juzi.flymsg.model.entity.UserInfo;
import com.juzi.flymsg.model.entity.UserLoginInfo;
import com.juzi.flymsg.service.UserInfoService;
import com.juzi.flymsg.mapper.UserInfoMapper;
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
    public Long userRegistry(String userAccount, String userPassword, String checkedPassword) {
        // 1、校验
//        a. 账号、密码、校验密码非空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkedPassword)) {
            throw new RuntimeException("参数异常");
        }
//        b. 账号长度 < 10
        if (userAccount.length() >= 10) {
            throw new RuntimeException("账号过长");
        }
//        c. 密码长度 >= 6
//        d. 校验密码长度 >= 6
        if (userPassword.length() < 6 || checkedPassword.length() < 6) {
            throw new RuntimeException("密码过短");
        }
//        e. 密码 == 校验密码
        if(!userPassword.equals(checkedPassword)) {
            throw new RuntimeException("两次输入密码不一致");
        }
//        f. 账号不能包含特殊符号
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new RuntimeException("账号包含特殊字符");
        }
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




