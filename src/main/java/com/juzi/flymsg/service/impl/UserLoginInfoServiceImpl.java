package com.juzi.flymsg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.flymsg.model.entity.UserLoginInfo;
import com.juzi.flymsg.service.UserLoginInfoService;
import com.juzi.flymsg.mapper.UserLoginInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author codejuzi
* @description 针对表【userLoginInfo(用户登录信息表)】的数据库操作Service实现
* @createDate 2023-04-02 20:28:12
*/
@Service
public class UserLoginInfoServiceImpl extends ServiceImpl<UserLoginInfoMapper, UserLoginInfo>
    implements UserLoginInfoService {

    /**
     * 盐值
     */
    private static final String SALT = "fly_msg";

    private static final String USER_LOGIN_STATE = "user_login";

    @Override
    public Long userLogin(String userAccount, String userPassword, HttpServletRequest request) {
//        a. 账号、密码非空
        if(StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new RuntimeException("参数异常");
        }
//        b. 账号长度 < 10
        if (userAccount.length() >= 10) {
            throw new RuntimeException("账号过长");
        }
//        c. 密码长度 >= 6
        if (userPassword.length() < 6) {
            throw new RuntimeException("密码过短");
        }
//        d. 账号不能包含特殊符号
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new RuntimeException("账号包含特殊字符");
        }
        // 2、根据userAccount查询数据库
        LambdaQueryWrapper<UserLoginInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLoginInfo::getUserAccount, userAccount);
        UserLoginInfo userLoginInfo = this.getOne(queryWrapper);
        if(userLoginInfo == null) {
            throw new RuntimeException("账号不存在");
        }
        // 3、比较密码
        String encryptPasswordInDb = userLoginInfo.getUserPassword();
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        if(!encryptPasswordInDb.equals(encryptPassword)) {
            throw new RuntimeException("密码不正确");
        }
        // 4、保存用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, userLoginInfo);
        // 5、返回用户id
        return userLoginInfo.getUserId();
    }

    @Override
    public UserLoginInfo getCurrentUser(HttpServletRequest request) {
        UserLoginInfo userLoginInfo = (UserLoginInfo) request.getSession().getAttribute(USER_LOGIN_STATE);
        if(userLoginInfo == null) {
            throw new RuntimeException("用户未登录");
        }
        return userLoginInfo;
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        // 移除用户登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }


}




