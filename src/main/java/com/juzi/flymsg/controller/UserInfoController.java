package com.juzi.flymsg.controller;

import com.juzi.flymsg.model.dto.UserLoginRequest;
import com.juzi.flymsg.model.dto.UserRegistryRequest;
import com.juzi.flymsg.model.entity.UserLoginInfo;
import com.juzi.flymsg.model.vo.UserInfoVO;
import com.juzi.flymsg.service.UserInfoService;
import com.juzi.flymsg.service.UserLoginInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    public Long userRegistry(@RequestBody UserRegistryRequest userRegistryRequest) {
        log.info("userRegistry....");

        String userAccount = userRegistryRequest.getUserAccount();
        String userPassword = userRegistryRequest.getUserPassword();
        String checkedPassword = userRegistryRequest.getCheckedPassword();

        // 简单校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkedPassword)) {
            throw new RuntimeException("参数异常");
        }

        return userInfoService.userRegistry(userRegistryRequest);
    }

    @PostMapping("/login")
    public Long userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        log.info("userLogin....");
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 简单校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new RuntimeException("参数异常");
        }
        return userLoginInfoService.userLogin(userLoginRequest, request);
    }

    @GetMapping("/current")
    public UserInfoVO getCurrentUser(HttpServletRequest request) {
        return userLoginInfoService.getCurrentUser(request);
    }

    @PostMapping("/logout")
    public Boolean userLogout(HttpServletRequest request) {
        return userLoginInfoService.userLogout(request);
    }
}
