package com.juzi.flymsg.controller;

import com.juzi.flymsg.common.BaseResponse;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.manager.UserManager;
import com.juzi.flymsg.model.dto.postfavour.PostFavourAddRequest;
import com.juzi.flymsg.model.vo.PostVO;
import com.juzi.flymsg.model.vo.UserInfoVO;
import com.juzi.flymsg.service.PostFavourService;
import com.juzi.flymsg.utils.ResultUtil;
import com.juzi.flymsg.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子收藏控制层
 *
 * @author codejuzi
 * @CreateTime 2023/4/9
 */
@Slf4j
@RestController
@RequestMapping("/post_favour")
public class PostFavourController {

    @Resource
    private PostFavourService postFavourService;

    @Resource
    private UserManager userManager;

    /**
     * 帖子收藏 / 取消收藏
     *
     * @param postFavourAddRequest 帖子收藏、取消收藏请求信息
     * @param request              request 请求域
     * @return resultNum 收藏变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doPostFavour(@RequestBody PostFavourAddRequest postFavourAddRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postFavourAddRequest == null || postFavourAddRequest.getPostId() <= 0, ErrorCode.PARAM_ERROR);
        // 获取当前登录用户
        UserInfoVO loginUserInfoVO = userManager.getLoginUser(request);
        ThrowUtil.throwIf(loginUserInfoVO == null, ErrorCode.NO_LOGIN);
        int result = postFavourService.doPostFavour(postFavourAddRequest, loginUserInfoVO);
        return ResultUtil.success(result);
    }

    @PostMapping("/list/my")
    public BaseResponse<List<PostVO>> listMyFavourPost(HttpServletRequest request) {
        List<PostVO> postVOList = postFavourService.listMyFavourPost(request);
        return ResultUtil.success(postVOList);
    }


}
