package com.juzi.flymsg.service;

import com.juzi.flymsg.model.dto.UserRegistryRequest;
import com.juzi.flymsg.model.dto.UserSelectRequest;
import com.juzi.flymsg.model.dto.UserUpdateRequest;
import com.juzi.flymsg.model.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juzi.flymsg.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author codejuzi
 * @description 针对表【userInfo(用户信息表)】的数据库操作Service
 * @createDate 2023-04-02 20:25:45
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户注册
     *
     * @param userRegistryRequest 用户注册请求对象信息
     * @return 用户id
     */
    Long userRegistry(UserRegistryRequest userRegistryRequest);

    /**
     * 删除用户，仅管理员可操作
     *
     * @param userId  待删除的user id
     * @param request request 域对象
     * @return true - 删除成功（逻辑删除）
     */
    boolean userDelete(Long userId, HttpServletRequest request);

    /**
     * 修改用户信息
     *
     * @param userUpdateRequest 用户修改请求信息
     * @param request           request域对象
     * @return true - 修改成功
     */
    boolean userUpdate(UserUpdateRequest userUpdateRequest, HttpServletRequest request);

    /**
     * 根据id查询单个用户对象
     *
     * @param userId 用户id
     * @return userVO
     */
    UserVO userSelectOne(Long userId);

    /**
     * 查询所有用户信息（脱敏）
     *
     * @param request request 域对象
     * @return 用户列表
     */
    List<UserVO> userListAll(HttpServletRequest request);

    /**
     * 根据关键词模糊查询 （by userName）
     *
     * @param searchText 搜索关键词
     * @param request    request 域对象
     * @return 用户列表
     */
    List<UserVO> userSelectByName(String searchText, HttpServletRequest request);
}
