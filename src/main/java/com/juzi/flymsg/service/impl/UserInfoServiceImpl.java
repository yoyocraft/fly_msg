package com.juzi.flymsg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.exception.BusinessException;
import com.juzi.flymsg.manager.UserManager;
import com.juzi.flymsg.mapper.UserInfoMapper;
import com.juzi.flymsg.mapper.UserLoginInfoMapper;
import com.juzi.flymsg.model.dto.user.UserRegistryRequest;
import com.juzi.flymsg.model.dto.user.UserUpdateRequest;
import com.juzi.flymsg.model.entity.UserInfo;
import com.juzi.flymsg.model.entity.UserLoginInfo;
import com.juzi.flymsg.model.vo.UserInfoVO;
import com.juzi.flymsg.model.vo.UserVO;
import com.juzi.flymsg.service.UserInfoService;
import com.juzi.flymsg.utils.ThrowUtil;
import com.juzi.flymsg.utils.ValidCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.juzi.flymsg.constant.UserConstant.SALT;

/**
 * @author codejuzi
 * @description 针对表【userInfo(用户信息表)】的数据库操作Service实现
 * @createDate 2023-04-02 20:25:45
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Resource
    private UserManager userManager;

    @Resource
    private UserLoginInfoMapper userLoginInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long userRegistry(UserRegistryRequest userRegistryRequest) {
        String userAccount = userRegistryRequest.getUserAccount();
        String userPassword = userRegistryRequest.getUserPassword();
        String checkedPassword = userRegistryRequest.getCheckedPassword();
        // 1、校验
        ValidCheckUtil.registryCheck(userAccount, userPassword, checkedPassword);
        // 每个账号只能注册一次
        synchronized (userAccount.intern()) {
            // g. 账号不能重复 => 查数据库
            // select id, userAccount, userPassword from userLoginInfo where userAccount = 'user1';
            UserLoginInfo userLoginInfo = userLoginInfoMapper.isExist(userAccount);
            ThrowUtil.throwIf(userLoginInfo != null, ErrorCode.PARAM_ERROR, "账号已经存在");

            //2、加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));


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

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public boolean userDelete(Long userId, HttpServletRequest request) {
        // 1、校验
        ThrowUtil.throwIf(userId == null || userId <= 0, ErrorCode.PARAM_ERROR);
        // 2、获取当前登录用户，并判断是否是管理员
        boolean isAdmin = userManager.isAdmin(request);
        ThrowUtil.throwIf(!isAdmin, ErrorCode.NO_AUTH);
        // 3、删除登录信息
        userLoginInfoMapper.deleteById(userId);
        // 删除用户信息
        return this.removeById(userId);
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public boolean userUpdate(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        // 1、校验
        ThrowUtil.throwIf(userUpdateRequest == null, ErrorCode.PARAM_ERROR);
        // 2、获取当前登录用户，判断是否是管理员
        UserInfoVO loginUserInfoVO = userManager.getLoginUser(request);
        Long loginUserId = loginUserInfoVO.getUserId();
        UserInfo loginUser = this.getById(loginUserId);
        Integer userRole = loginUser.getUserRole();
        // 单个用户的串行修改
        synchronized (String.valueOf(loginUserId).intern()) {
            // 判断是否是本人 || 是否是管理员
            Long userId = userUpdateRequest.getId();
            boolean isMe = loginUserId.equals(userId);
            boolean isAdmin = userRole != null && userRole == 1;
            ThrowUtil.throwIf(!isAdmin && !isMe, ErrorCode.NO_AUTH);
            // 是本人 || 是管理员
            String userPassword = userUpdateRequest.getUserPassword();
            String userName = userUpdateRequest.getUserName();
            String userAvatar = userUpdateRequest.getUserAvatar();
            String userProfile = userUpdateRequest.getUserProfile();
            LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserInfo::getId, userId);
            if (StringUtils.isNotBlank(userPassword)) {
                String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
                updateWrapper.set(UserInfo::getUserPassword, encryptPassword);
                // 修改userLoginInfo中的密码
                userLoginInfoMapper.updateUserPasswordBoolean(userId, encryptPassword);
            }
            updateWrapper.set(StringUtils.isNotBlank(userAvatar), UserInfo::getUserAvatar, userAvatar);
            updateWrapper.set(StringUtils.isNotBlank(userName), UserInfo::getUserName, userName);
            updateWrapper.set(StringUtils.isNotBlank(userProfile), UserInfo::getUserProfile, userProfile);

            // 修改userInfo中的用户信息
            return this.update(updateWrapper);
        }
    }

    @Override
    public UserVO userSelectById(Long userId) {
        ThrowUtil.throwIf(userId == null || userId <= 0, ErrorCode.PARAM_ERROR);
        UserInfo userInfo = this.getById(userId);
        // 脱敏
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userInfo, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> userListAll(HttpServletRequest request) {
        // 1、获取当前登录用户
        boolean isAdmin = userManager.isAdmin(request);
        ThrowUtil.throwIf(!isAdmin, ErrorCode.NO_AUTH);
        // 2、查询所有用户
        List<UserInfo> userInfoList = this.list();
        // Java8新特性 stream流
        return userInfoList.stream().map(userInfo -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(userInfo, userVO);
            return userVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserVO> userSelectByName(String searchText, HttpServletRequest request) {
        ThrowUtil.throwIf(StringUtils.isBlank(searchText), ErrorCode.PARAM_ERROR);
        // 获取当前登录用户
        UserInfoVO currentUser = userManager.getLoginUser(request);
        ThrowUtil.throwIf(currentUser == null, ErrorCode.NO_LOGIN, "需要登录后才能查询");
        // select * from userInfo where userName like '%管理%';
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(UserInfo::getUserName, searchText);
        List<UserInfo> userInfoList = this.list(queryWrapper);

        return userInfoList.stream().map(userInfo -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(userInfo, userVO);
            return userVO;
        }).collect(Collectors.toList());
    }
}




