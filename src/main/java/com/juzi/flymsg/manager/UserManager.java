package com.juzi.flymsg.manager;

import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.exception.BusinessException;
import com.juzi.flymsg.model.entity.UserLoginInfo;
import com.juzi.flymsg.model.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.juzi.flymsg.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户管理层
 *
 * @author codejuzi
 * @CreateTime 2023/4/5
 */
@Component
public class UserManager {
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
}
