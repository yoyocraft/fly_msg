package com.juzi.flymsg.controller;

import com.juzi.flymsg.common.BaseResponse;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.model.dto.post.PostAddRequest;
import com.juzi.flymsg.model.dto.post.PostDeleteRequest;
import com.juzi.flymsg.model.dto.post.PostUpdateRequest;
import com.juzi.flymsg.model.entity.Post;
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
    public BaseResponse<Post> postSelectById(@PathVariable(value = "id") Long postId) {
        ThrowUtil.throwIf(postId == null, ErrorCode.PARAM_ERROR);
        Post post = postService.postSelectById(postId);
        return ResultUtil.success(post);
    }

    @GetMapping("/list/all")
    public BaseResponse<List<Post>> postListAll(HttpServletRequest request) {
        List<Post> postList = postService.postListAll(request);
        return ResultUtil.success(postList);
    }

    @GetMapping("/list/{id}")
    public BaseResponse<List<Post>> postListByUserId(@PathVariable(value = "id") Long userId) {
        ThrowUtil.throwIf(userId == null, ErrorCode.PARAM_ERROR);
        List<Post> postList = postService.postListByUserId(userId);
        return ResultUtil.success(postList);
    }

    @GetMapping("/select/title")
    public BaseResponse<List<Post>> postListByTitle(@RequestParam(value = "title") String searchText) {
        ThrowUtil.throwIf(StringUtils.isBlank(searchText), ErrorCode.PARAM_ERROR);
        List<Post> postList = postService.postListByTitle(searchText);
        return ResultUtil.success(postList);
    }

    @GetMapping("/list/tags")
    public BaseResponse<List<Post>> postListByTags(@RequestParam List<String> tagList) {
        ThrowUtil.throwIf(tagList == null, ErrorCode.PARAM_ERROR);
        List<Post> postList = postService.postListByTags(tagList);
        return ResultUtil.success(postList);
    }

    @GetMapping("/list/content")
    public BaseResponse<List<Post>> postListByContent(@RequestParam(value = "content") String searchText) {
        ThrowUtil.throwIf(searchText == null, ErrorCode.PARAM_ERROR);
        List<Post> postList = postService.postListByContent(searchText);
        return ResultUtil.success(postList);
    }
}
