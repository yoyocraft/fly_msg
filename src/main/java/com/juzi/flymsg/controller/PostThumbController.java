package com.juzi.flymsg.controller;

import com.juzi.flymsg.common.BaseResponse;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.manager.UserManager;
import com.juzi.flymsg.model.dto.pagethumb.PostThumbAddRequest;
import com.juzi.flymsg.model.vo.PostVO;
import com.juzi.flymsg.model.vo.UserInfoVO;
import com.juzi.flymsg.service.PostThumbService;
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
 * 帖子点赞控制层
 *
 * @author codejuzi
 * @CreateTime 2023/4/9
 */
@Slf4j
@RestController
@RequestMapping("/post_thumb")
public class PostThumbController {

    @Resource
    private PostThumbService postThumbService;

    @Resource
    private UserManager userManager;

    /**
     * 帖子点赞 / 取消点赞
     *
     * @param postThumbAddRequest 帖子点赞、取消点赞请求信息
     * @param request              request 请求域
     * @return resultNum 点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doPostThumb(@RequestBody PostThumbAddRequest postThumbAddRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0, ErrorCode.PARAM_ERROR);
        // 获取当前登录用户
        UserInfoVO loginUserInfoVO = userManager.getLoginUser(request);
        ThrowUtil.throwIf(loginUserInfoVO == null, ErrorCode.NO_LOGIN);
        int result = postThumbService.doPostThumb(postThumbAddRequest, loginUserInfoVO);
        return ResultUtil.success(result);
    }

    @PostMapping("/list/my")
    public BaseResponse<List<PostVO>> listMyThumbPost(HttpServletRequest request) {
        List<PostVO> postVOList = postThumbService.listMyThumbPost(request);
        return ResultUtil.success(postVOList);
    }


}
