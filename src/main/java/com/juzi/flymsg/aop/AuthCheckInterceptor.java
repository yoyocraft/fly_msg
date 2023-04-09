package com.juzi.flymsg.aop;

import com.juzi.flymsg.annotation.AuthCheck;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.manager.UserManager;
import com.juzi.flymsg.model.enums.UserRoleEnum;
import com.juzi.flymsg.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验拦截器
 *
 * @author codejuzi
 * @CreateTime 2023/4/9
 */
@Slf4j
@Aspect
@Component
public class AuthCheckInterceptor {

    @Resource
    private UserManager userManager;

    /**
     * 执行拦截校验
     *
     * @param joinPoint 切点
     * @param authCheck 注解
     * @return 执行结果
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        int mustRole = authCheck.mustRole();
        ThrowUtil.throwIf(mustRole < 0, ErrorCode.PARAM_ERROR, "mustRole >= 0");

        // 获取当前登录用户的角色的枚举值
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        UserRoleEnum loginUserRoleEnum = userManager.getUserRole(request);

        // 如果是被封号的账号，直接拒绝
        ThrowUtil.throwIf(UserRoleEnum.BAN.equals(loginUserRoleEnum), ErrorCode.NO_AUTH, "当前账号已被封号");

        // 获取当前方法必须要有的权限
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        ThrowUtil.throwIf(mustRoleEnum == null, ErrorCode.NO_AUTH, "角色不存在【管理员、用户、封号】");

        // 必须有管理员权限 && 当前角色不是管理员
        boolean isValid = UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(loginUserRoleEnum);
        ThrowUtil.throwIf(isValid, ErrorCode.NO_AUTH, "不是管理员，无权操作");
        // 放行
        return joinPoint.proceed();
    }
}
