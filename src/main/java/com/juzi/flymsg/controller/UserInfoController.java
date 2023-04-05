package com.juzi.flymsg.controller;

import com.juzi.flymsg.common.BaseResponse;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.exception.BusinessException;
import com.juzi.flymsg.manager.UserManager;
import com.juzi.flymsg.model.dto.UserLoginRequest;
import com.juzi.flymsg.model.dto.UserRegistryRequest;
import com.juzi.flymsg.model.dto.UserSelectRequest;
import com.juzi.flymsg.model.dto.UserUpdateRequest;
import com.juzi.flymsg.model.vo.UserInfoVO;
import com.juzi.flymsg.model.vo.UserVO;
import com.juzi.flymsg.service.UserInfoService;
import com.juzi.flymsg.service.UserLoginInfoService;
import com.juzi.flymsg.utils.ResultUtil;
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
    private UserManager userManager;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserLoginInfoService userLoginInfoService;

    @PostMapping("/registry")
    public BaseResponse<Long> userRegistry(@RequestBody UserRegistryRequest userRegistryRequest) {
        log.info("userRegistry....");

        String userAccount = userRegistryRequest.getUserAccount();
        String userPassword = userRegistryRequest.getUserPassword();
        String checkedPassword = userRegistryRequest.getCheckedPassword();

        // 简单校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkedPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }

        Long userId = userInfoService.userRegistry(userRegistryRequest);
        return ResultUtil.success(userId);
    }

    @PostMapping("/login")
    public BaseResponse<Long> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        log.info("userLogin....");
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 简单校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        Long userId = userLoginInfoService.userLogin(userLoginRequest, request);
        return ResultUtil.success(userId);
    }

    @GetMapping("/current")
    public BaseResponse<UserInfoVO> getCurrentUser(HttpServletRequest request) {
        UserInfoVO currentUser = userManager.getCurrentUser(request);
        return ResultUtil.success(currentUser);
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        Boolean flag = userLoginInfoService.userLogout(request);
        return ResultUtil.success(flag);
    }

    @DeleteMapping("/delete")
    public BaseResponse<Boolean> userDelete(Long userId, HttpServletRequest request) {
        // 做一些简单校验
        if(userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        boolean flag = userInfoService.userDelete(userId, request);
        return ResultUtil.success(flag);
    }

    @PutMapping("/update")
    public BaseResponse<Boolean> userUpdate(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if(userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        boolean flag = userInfoService.userUpdate(userUpdateRequest, request);
        return ResultUtil.success(flag);
    }

    @GetMapping("/select/{id}")
    public BaseResponse<UserVO> userSelectOne(@PathVariable(value = "id") Long userId) {
        if(userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        UserVO userVO = userInfoService.userSelectOne(userId);
        return ResultUtil.success(userVO);
    }
}
