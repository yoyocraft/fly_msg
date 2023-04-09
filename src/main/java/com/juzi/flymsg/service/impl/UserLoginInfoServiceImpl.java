package com.juzi.flymsg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.manager.UserManager;
import com.juzi.flymsg.mapper.UserLoginInfoMapper;
import com.juzi.flymsg.model.dto.user.UserLoginRequest;
import com.juzi.flymsg.model.entity.UserLoginInfo;
import com.juzi.flymsg.model.vo.UserInfoVO;
import com.juzi.flymsg.service.UserLoginInfoService;
import com.juzi.flymsg.utils.ThrowUtil;
import com.juzi.flymsg.utils.ValidCheckUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

import static com.juzi.flymsg.constant.UserConstant.SALT;
import static com.juzi.flymsg.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author codejuzi
 * @description 针对表【userLoginInfo(用户登录信息表)】的数据库操作Service实现
 * @createDate 2023-04-02 20:28:12
 */
@Service
public class UserLoginInfoServiceImpl extends ServiceImpl<UserLoginInfoMapper, UserLoginInfo>
        implements UserLoginInfoService {

    @Resource
    private UserManager userManager;

    @Override
    public Long userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 1、校验
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        ValidCheckUtil.commonLoginRegistryCheck(userAccount, userPassword);
        // 加锁，每个账号串行执行
        // 判断是否登录
        UserInfoVO loginUser = userManager.getLoginUser(request);
        if(loginUser == null) {
            synchronized (userAccount.intern()) {
                loginUser = userManager.getLoginUser(request);
                if(loginUser != null) {
                    return loginUser.getUserId();
                }
                // 2、根据userAccount查询数据库
                LambdaQueryWrapper<UserLoginInfo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(UserLoginInfo::getUserAccount, userAccount);
                UserLoginInfo userLoginInfo = this.getOne(queryWrapper);
                ThrowUtil.throwIf(userLoginInfo == null, ErrorCode.NOT_FOUND, "账号未注册");
                // 3、比较密码
                String encryptPasswordInDb = userLoginInfo.getUserPassword();
                String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
                ThrowUtil.throwIf(!encryptPasswordInDb.equals(encryptPassword), ErrorCode.PARAM_ERROR, "密码输入错误！");
                // 4、保存用户登录态
                request.getSession().setAttribute(USER_LOGIN_STATE, userLoginInfo);
                // 5、返回用户id
                return userLoginInfo.getUserId();
            }
        }
        return loginUser.getUserId();
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        // 移除用户登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }


}




