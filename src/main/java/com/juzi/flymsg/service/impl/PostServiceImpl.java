package com.juzi.flymsg.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.manager.UserManager;
import com.juzi.flymsg.mapper.PostMapper;
import com.juzi.flymsg.model.dto.post.PostAddRequest;
import com.juzi.flymsg.model.dto.post.PostDeleteRequest;
import com.juzi.flymsg.model.dto.post.PostSelectRequest;
import com.juzi.flymsg.model.dto.post.PostUpdateRequest;
import com.juzi.flymsg.model.entity.Post;
import com.juzi.flymsg.model.vo.UserInfoVO;
import com.juzi.flymsg.service.PostService;
import com.juzi.flymsg.utils.ThrowUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

/**
 * @author codejuzi
 * @description 针对表【post(帖子文章)】的数据库操作Service实现
 * @createDate 2023-04-07 20:15:18
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
        implements PostService {

    @Resource
    private UserManager userManager;

    @Resource
    private PostMapper postMapper;

    @Override
    public Long postAdd(PostAddRequest postAddRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postAddRequest == null, ErrorCode.PARAM_ERROR);
        // 获取当前登录用户
        UserInfoVO loginUserInfoVO = userManager.getCurrentUser(request);
        ThrowUtil.throwIf(loginUserInfoVO == null, ErrorCode.NO_LOGIN);

        // 校验
        String title = postAddRequest.getTitle();
        String content = postAddRequest.getContent();
        String tags = postAddRequest.getTags();
        ThrowUtil.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAM_ERROR);
        // 转小写
        tags = tags.toLowerCase(Locale.ROOT);
        postAddRequest.setTags(tags);
        // 新增
        Post post = new Post();
        BeanUtils.copyProperties(postAddRequest, post);
        post.setUserId(loginUserInfoVO.getUserId());
        this.save(post);

        return post.getId();
    }

    @Override
    public boolean postUpdate(PostUpdateRequest postUpdateRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postUpdateRequest == null, ErrorCode.PARAM_ERROR);
        // 获取当前登录用户
        UserInfoVO loginUserInfoVO = userManager.getCurrentUser(request);
        // 判断是否是本人
        Long loginUserId = loginUserInfoVO.getUserId();
        Long userId = postUpdateRequest.getUserId();
        ThrowUtil.throwIf(!loginUserId.equals(userId), ErrorCode.NO_AUTH);
        // 修改
        Long id = postUpdateRequest.getId();
        String title = postUpdateRequest.getTitle();
        String content = postUpdateRequest.getContent();
        String tags = postUpdateRequest.getTags();
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(id != null, Post::getId, id);
        updateWrapper.set(StringUtils.isNotBlank(title), Post::getTitle, title);
        updateWrapper.set(StringUtils.isNotBlank(content), Post::getContent, content);
        updateWrapper.set(StringUtils.isNotBlank(tags), Post::getTags, tags);

        return this.update(updateWrapper);
    }

    @Override
    public boolean postDelete(PostDeleteRequest postDeleteRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postDeleteRequest == null, ErrorCode.PARAM_ERROR);
        // 获取当前登录用户
        UserInfoVO loginUserInfoVO = userManager.getCurrentUser(request);
        Long loginUserId = loginUserInfoVO.getUserId();
        Long userId = postDeleteRequest.getUserId();
        boolean isAdmin = userManager.isAdmin(request);
        boolean isMe = loginUserId.equals(userId);
        // 不是管理员 && 不是本人
        ThrowUtil.throwIf(!isAdmin && !isMe, ErrorCode.NO_AUTH);
        // 删除帖子
        Long postId = postDeleteRequest.getId();
        ThrowUtil.throwIf(postId == null, ErrorCode.PARAM_ERROR);
        return this.removeById(postId);
    }

    @Override
    public Post postSelectById(Long postId) {
        ThrowUtil.throwIf(postId == null, ErrorCode.PARAM_ERROR);
        return postMapper.postSelectById(postId);
    }

    @Override
    public List<Post> postListAll(HttpServletRequest request) {
        // 判断是否是管理员
        boolean isAdmin = userManager.isAdmin(request);
        ThrowUtil.throwIf(!isAdmin, ErrorCode.NO_AUTH);
        // 查询
        return this.list();
    }

    @Override
    public List<Post> postListByUserId(Long userId) {
        ThrowUtil.throwIf(userId == null, ErrorCode.PARAM_ERROR);
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getUserId, userId);
        return this.list(queryWrapper);
    }

    @Override
    public List<Post> postListByTitle(String searchText) {
        ThrowUtil.throwIf(StringUtils.isBlank(searchText), ErrorCode.PARAM_ERROR);
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Post::getTitle, searchText);
        return this.list(queryWrapper);
    }

    @Override
    public List<Post> postListByTags(List<String> tagList) {
        ThrowUtil.throwIf(tagList == null, ErrorCode.PARAM_ERROR);
        String tags = JSONUtil.toJsonStr(tagList);
        tags = tags.toLowerCase(Locale.ROOT);
        // 查询
        return postMapper.postListByTags(tags);
    }

    @Override
    public List<Post> postListByContent(String content) {
        ThrowUtil.throwIf(StringUtils.isBlank(content), ErrorCode.PARAM_ERROR);
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Post::getContent, content);
        return this.list(queryWrapper);
    }

    @Override
    public List<Post> postListByContentAndTags(PostSelectRequest postSelectRequest) {
        ThrowUtil.throwIf(postSelectRequest == null, ErrorCode.PARAM_ERROR);
        List<String> tagList = postSelectRequest.getTagList();
        String content = postSelectRequest.getContent();
        String tags = JSONUtil.toJsonStr(tagList);
        tags = tags.toLowerCase(Locale.ROOT);
        // 查询
        return postMapper.postListByContentAndTags(content, tags);
    }
}




