package com.juzi.flymsg.controller;

import com.juzi.flymsg.model.entity.UserLoginInfo;
import com.juzi.flymsg.service.UserInfoService;
import com.juzi.flymsg.service.UserLoginInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author codejuzi
 * @CreateTime 2023/4/2
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserLoginInfoService userLoginInfoService;

    @PostMapping("/registry")
    public Long userRegistry(String userAccount, String userPassword, String checkedPassword) {
        log.info("userRegistry....");
        // 简单校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkedPassword)) {
            throw new RuntimeException("参数异常");
        }

        return userInfoService.userRegistry(userAccount, userPassword, checkedPassword);
    }

    @PostMapping("/login")
    public Long userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        log.info("userLogin....");
        // 简单校验
        if(StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new RuntimeException("参数异常");
        }
        return userLoginInfoService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/current")
    public UserLoginInfo getCurrentUser(HttpServletRequest request) {
        return userLoginInfoService.getCurrentUser(request);
    }

    @PostMapping("/logout")
    public Boolean userLogout(HttpServletRequest request) {
        return userLoginInfoService.userLogout(request);
    }
}
