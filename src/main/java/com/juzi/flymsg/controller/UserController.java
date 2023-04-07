package com.juzi.flymsg.controller;

import com.juzi.flymsg.common.BaseResponse;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.manager.UserManager;
import com.juzi.flymsg.model.dto.user.UserLoginRequest;
import com.juzi.flymsg.model.dto.user.UserRegistryRequest;
import com.juzi.flymsg.model.dto.user.UserUpdateRequest;
import com.juzi.flymsg.model.vo.UserInfoVO;
import com.juzi.flymsg.model.vo.UserVO;
import com.juzi.flymsg.service.UserInfoService;
import com.juzi.flymsg.service.UserLoginInfoService;
import com.juzi.flymsg.utils.ResultUtil;
import com.juzi.flymsg.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author codejuzi
 * @CreateTime 2023/4/2
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

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
        ThrowUtil.throwIf(StringUtils.isAnyBlank(userAccount, userPassword, checkedPassword), ErrorCode.PARAM_ERROR);

        Long userId = userInfoService.userRegistry(userRegistryRequest);
        return ResultUtil.success(userId);
    }

    @PostMapping("/login")
    public BaseResponse<Long> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        log.info("userLogin....");
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 简单校验
        ThrowUtil.throwIf(StringUtils.isAnyBlank(userAccount, userPassword), ErrorCode.PARAM_ERROR);
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

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> userDelete(@PathVariable(value = "id") Long userId, HttpServletRequest request) {
        // 做一些简单校验
        ThrowUtil.throwIf(userId == null || userId <= 0, ErrorCode.PARAM_ERROR);
        boolean flag = userInfoService.userDelete(userId, request);
        return ResultUtil.success(flag);
    }

    @PutMapping("/update")
    public BaseResponse<Boolean> userUpdate(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(userUpdateRequest == null, ErrorCode.PARAM_ERROR);
        boolean flag = userInfoService.userUpdate(userUpdateRequest, request);
        return ResultUtil.success(flag);
    }

    @GetMapping("/select/{id}")
    public BaseResponse<UserVO> userSelectById(@PathVariable(value = "id") Long userId) {
        ThrowUtil.throwIf(userId == null || userId <= 0, ErrorCode.PARAM_ERROR);
        UserVO userVO = userInfoService.userSelectById(userId);
        return ResultUtil.success(userVO);
    }

    @GetMapping("/list")
    public BaseResponse<List<UserVO>> userListAll(HttpServletRequest request) {
        List<UserVO> userVOList = userInfoService.userListAll(request);
        return ResultUtil.success(userVOList);
    }

    @GetMapping("/select/name")
    public BaseResponse<List<UserVO>> userSelectByName(String searchText, HttpServletRequest request) {
        ThrowUtil.throwIf(StringUtils.isBlank(searchText), ErrorCode.PARAM_ERROR);
        List<UserVO> userVOList = userInfoService.userSelectByName(searchText, request);
        return ResultUtil.success(userVOList);
    }
}
