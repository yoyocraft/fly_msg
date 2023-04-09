package com.juzi.flymsg.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juzi.flymsg.annotation.AuthCheck;
import com.juzi.flymsg.common.BaseResponse;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.common.PageRequest;
import com.juzi.flymsg.constant.UserConstant;
import com.juzi.flymsg.model.dto.post.PostAddRequest;
import com.juzi.flymsg.model.dto.post.PostDeleteRequest;
import com.juzi.flymsg.model.dto.post.PostQueryRequest;
import com.juzi.flymsg.model.dto.post.PostUpdateRequest;
import com.juzi.flymsg.model.vo.PostVO;
import com.juzi.flymsg.service.PostService;
import com.juzi.flymsg.utils.ResultUtil;
import com.juzi.flymsg.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子文章控制器
 *
 * @author codejuzi
 * @CreateTime 2023/4/7
 */
@Slf4j
@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private PostService postService;

    @PostMapping("/add")
    public BaseResponse<Long> postAdd(@RequestBody PostAddRequest postAddRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postAddRequest == null, ErrorCode.PARAM_ERROR);
        Long postId = postService.postAdd(postAddRequest, request);
        return ResultUtil.success(postId);
    }

    @PutMapping("/update")
    public BaseResponse<Boolean> postUpdate(@RequestBody PostUpdateRequest postUpdateRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postUpdateRequest == null, ErrorCode.PARAM_ERROR);
        boolean res = postService.postUpdate(postUpdateRequest, request);
        return ResultUtil.success(res);
    }

    @DeleteMapping("/delete")
    public BaseResponse<Boolean> postDelete(@RequestBody PostDeleteRequest postDeleteRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postDeleteRequest == null, ErrorCode.PARAM_ERROR);
        boolean res = postService.postDelete(postDeleteRequest, request);
        return ResultUtil.success(res);
    }

    @GetMapping("/select/{id}")
    public BaseResponse<PostVO> postSelectById(@PathVariable(value = "id") Long postId, HttpServletRequest request) {
        ThrowUtil.throwIf(postId == null, ErrorCode.PARAM_ERROR);
        PostVO postVO = postService.postSelectById(postId, request);
        return ResultUtil.success(postVO);
    }

    @PostMapping("/list/all")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<PostVO>> postListAll(@RequestBody PageRequest pageRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(pageRequest == null, ErrorCode.PARAM_ERROR);
        Page<PostVO> postVOPage = postService.postListAllByPage(pageRequest, request);
        return ResultUtil.success(postVOPage);
    }

    @GetMapping("/list/{id}")
    public BaseResponse<List<PostVO>> postListByUserId(@PathVariable(value = "id") Long userId, HttpServletRequest request) {
        ThrowUtil.throwIf(userId == null, ErrorCode.PARAM_ERROR);
        List<PostVO> postVOList = postService.postListByUserId(userId, request);
        return ResultUtil.success(postVOList);
    }

    @GetMapping("/select/vague")
    public BaseResponse<List<PostVO>> postListByTitleOrContent(@RequestParam(value = "title") String searchText, HttpServletRequest request) {
        ThrowUtil.throwIf(StringUtils.isBlank(searchText), ErrorCode.PARAM_ERROR);
        List<PostVO> postVOList = postService.postListByTitleOrContent(searchText, request);
        return ResultUtil.success(postVOList);
    }

    @GetMapping("/list/tags")
    public BaseResponse<List<PostVO>> postListByTags(@RequestParam List<String> tagList, HttpServletRequest request) {
        ThrowUtil.throwIf(tagList == null, ErrorCode.PARAM_ERROR);
        List<PostVO> postVOList = postService.postListByTags(tagList, request);
        return ResultUtil.success(postVOList);
    }

    @PostMapping("/list/vague")
    public BaseResponse<Page<PostVO>> postListByContentAndTags(@RequestBody PostQueryRequest postQueryRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postQueryRequest == null, ErrorCode.PARAM_ERROR);
        Page<PostVO> postVOList = postService.postListWithContentAndTagsByPage(postQueryRequest, request);
        return ResultUtil.success(postVOList);
    }
}
