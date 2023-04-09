package com.juzi.flymsg.manager;

import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.exception.BusinessException;
import com.juzi.flymsg.mapper.UserInfoMapper;
import com.juzi.flymsg.model.entity.UserInfo;
import com.juzi.flymsg.model.entity.UserLoginInfo;
import com.juzi.flymsg.model.enums.UserRoleEnum;
import com.juzi.flymsg.model.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.juzi.flymsg.constant.UserConstant.ADMIN_ROLE;
import static com.juzi.flymsg.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户管理层
 *
 * @author codejuzi
 * @CreateTime 2023/4/5
 */
@Component
public class UserManager {

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 获取当前登录用户
     *
     * @param request request域对象
     * @return 脱敏后的用户登录信息
     */
    public UserInfoVO getCurrentUser(HttpServletRequest request) {
        UserLoginInfo userLoginInfo = (UserLoginInfo) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userLoginInfo == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userLoginInfo, userInfoVO);
        return userInfoVO;
    }

    /**
     * 判断当前登录用户是否是管理员
     *
     * @param request request 域对象
     * @return true - 是管理员
     */
    public boolean isAdmin(HttpServletRequest request) {
        UserInfoVO loginUserInfoVO = this.getCurrentUser(request);
        Long loginUserId = loginUserInfoVO.getUserId();
        UserInfo loginUser = userInfoMapper.selectById(loginUserId);
        Integer userRole = loginUser.getUserRole();
        return userRole != null && userRole == ADMIN_ROLE;
    }

    /**
     * 获取当前登录用户的角色枚举值
     *
     * @param request request 域对象
     * @return UserRoleEnum
     */
    public UserRoleEnum getUserRole(HttpServletRequest request) {
        UserInfoVO loginUserInfoVO = this.getCurrentUser(request);
        Long loginUserId = loginUserInfoVO.getUserId();
        UserInfo loginUser = userInfoMapper.selectById(loginUserId);
        return UserRoleEnum.getEnumByValue(loginUser.getUserRole());
    }
}
