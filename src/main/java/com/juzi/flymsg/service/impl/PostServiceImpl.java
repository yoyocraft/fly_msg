package com.juzi.flymsg.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.common.PageRequest;
import com.juzi.flymsg.constant.CommonConstant;
import com.juzi.flymsg.manager.UserManager;
import com.juzi.flymsg.mapper.PostFavourMapper;
import com.juzi.flymsg.mapper.PostMapper;
import com.juzi.flymsg.mapper.PostThumbMapper;
import com.juzi.flymsg.mapper.UserInfoMapper;
import com.juzi.flymsg.model.dto.post.PostAddRequest;
import com.juzi.flymsg.model.dto.post.PostDeleteRequest;
import com.juzi.flymsg.model.dto.post.PostQueryRequest;
import com.juzi.flymsg.model.dto.post.PostUpdateRequest;
import com.juzi.flymsg.model.entity.Post;
import com.juzi.flymsg.model.entity.PostFavour;
import com.juzi.flymsg.model.entity.PostThumb;
import com.juzi.flymsg.model.entity.UserInfo;
import com.juzi.flymsg.model.vo.PostVO;
import com.juzi.flymsg.model.vo.UserInfoVO;
import com.juzi.flymsg.model.vo.UserVO;
import com.juzi.flymsg.service.PostService;
import com.juzi.flymsg.utils.SqlUtils;
import com.juzi.flymsg.utils.ThrowUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private PostThumbMapper postThumbMapper;

    @Resource
    private PostFavourMapper postFavourMapper;

    @Override
    public Long postAdd(PostAddRequest postAddRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postAddRequest == null, ErrorCode.PARAM_ERROR);
        // 获取当前登录用户
        UserInfoVO loginUserInfoVO = userManager.getLoginUser(request);
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
        UserInfoVO loginUserInfoVO = userManager.getLoginUser(request);
        // 单人修改串行执行
        Long loginUserId = loginUserInfoVO.getUserId();
        synchronized (String.valueOf(loginUserId).intern()) {
            // 判断是否是本人
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
    }

    @Override
    public boolean postDelete(PostDeleteRequest postDeleteRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postDeleteRequest == null, ErrorCode.PARAM_ERROR);
        // 获取当前登录用户
        UserInfoVO loginUserInfoVO = userManager.getLoginUser(request);
        Long loginUserId = loginUserInfoVO.getUserId();
        synchronized (String.valueOf(loginUserId)) {
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
    }

    @Override
    public PostVO postSelectById(Long postId, HttpServletRequest request) {
        ThrowUtil.throwIf(postId == null, ErrorCode.PARAM_ERROR);
        Post post = postMapper.postSelectById(postId);
        return this.getPostVO(post, request);
    }

    @Override
    public Page<PostVO> postListAllByPage(PageRequest pageRequest, HttpServletRequest request) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        String sortOrder = pageRequest.getSortOrder();
        String sortField = pageRequest.getSortField();
        // 限制爬虫
        ThrowUtil.throwIf(pageSize > 20, ErrorCode.PARAM_ERROR, "一次请求过多！");
        // 判断是否是管理员
        boolean isAdmin = userManager.isAdmin(request);
        ThrowUtil.throwIf(!isAdmin, ErrorCode.NO_AUTH);
        // 查询
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        Page<Post> postPage = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<PostVO> postVOList = postPage.getRecords().stream()
                .map(post -> this.getPostVO(post, request))
                .collect(Collectors.toList());
        // 转换成 Page<PostVO>
        Page<PostVO> postVOPage = new Page<>(pageNum, pageSize, postVOList.size());
        postVOPage.setRecords(postVOList);
        return postVOPage;
    }

    @Override
    public List<PostVO> postListByUserId(Long userId, HttpServletRequest request) {
        ThrowUtil.throwIf(userId == null, ErrorCode.PARAM_ERROR);
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getUserId, userId);
        List<Post> postList = this.list(queryWrapper);
        return postList.stream().map(post -> this.getPostVO(post, request)).collect(Collectors.toList());
    }

    @Override
    public List<PostVO> postListByTitleOrContent(String searchText, HttpServletRequest request) {
        ThrowUtil.throwIf(StringUtils.isBlank(searchText), ErrorCode.PARAM_ERROR);
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Post::getTitle, searchText).or().like(Post::getContent, searchText);
        List<Post> postList = this.list(queryWrapper);
        return postList.stream().map(post -> this.getPostVO(post, request)).collect(Collectors.toList());
    }

    @Override
    public List<PostVO> postListByTags(List<String> tagList, HttpServletRequest request) {
        ThrowUtil.throwIf(tagList == null, ErrorCode.PARAM_ERROR);
        String tags = JSONUtil.toJsonStr(tagList);
        tags = tags.toLowerCase(Locale.ROOT);
        // 查询
        List<Post> postList = postMapper.postListByTags(tags);
        return postList.stream().map(post -> this.getPostVO(post, request)).collect(Collectors.toList());
    }

    @Override
    public Page<PostVO> postListWithContentAndTagsByPage(PostQueryRequest postQueryRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(postQueryRequest == null, ErrorCode.PARAM_ERROR);
        // 取值
        List<String> tagList = postQueryRequest.getTagList();
        String content = postQueryRequest.getContent();
        int pageNum = postQueryRequest.getPageNum();
        int pageSize = postQueryRequest.getPageSize();
        String sortField = postQueryRequest.getSortField();
        String sortOrder = postQueryRequest.getSortOrder();
        String tags = JSONUtil.toJsonStr(tagList);
        tags = tags.toLowerCase(Locale.ROOT);
        // 限制爬虫
        ThrowUtil.throwIf(pageSize > 20, ErrorCode.PARAM_ERROR, "一次请求过多！");
        // 校验
        boolean validSortField = SqlUtils.validSortField(sortField);
        if (!validSortField) {
            sortField = CommonConstant.DEFAULT_SORTED_FIELD;
        }
        boolean isASC = sortOrder.equals(CommonConstant.SORT_ORDER_ASC);
        // 查询
        long offset = (long) (pageNum - 1) * pageSize;
        List<Post> postList = postMapper.postListWithContentAndTagsByPage(content, tags, offset, pageSize, sortField, isASC);
        List<PostVO> postVOList = postList.stream()
                .map(post -> this.getPostVO(post, request))
                .collect(Collectors.toList());
        Page<PostVO> postVOPage = new Page<>(pageNum, pageSize, postVOList.size());
        postVOPage.setRecords(postVOList);
        return postVOPage;
    }

    @Override
    public PostVO getPostVO(Post post, HttpServletRequest request) {
        PostVO postVO = PostVO.objToVo(post);
        long postId = post.getId();
        // 1. 关联查询用户信息
        Long userId = post.getUserId();
        UserInfo user = null;
        if (userId != null && userId > 0) {
            user = userInfoMapper.selectById(userId);
        }
        UserVO userVO = userManager.getUserVO(user);
        postVO.setUser(userVO);
        // 获取当前登录用户
        UserInfoVO loginUserInfoVO = userManager.getLoginUserPermitNull(request);
        if (loginUserInfoVO != null) {
            // 获取点赞
            LambdaQueryWrapper<PostThumb> postThumbQueryWrapper = new LambdaQueryWrapper<>();
            postThumbQueryWrapper.eq(PostThumb::getPostId, postId);
            postThumbQueryWrapper.eq(PostThumb::getUserId, loginUserInfoVO.getUserId());
            PostThumb postThumb = postThumbMapper.selectOne(postThumbQueryWrapper);
            postVO.setHasThumb(postThumb != null);
            // 获取收藏
            LambdaQueryWrapper<PostFavour> postFavourQueryWrapper = new LambdaQueryWrapper<>();
            postFavourQueryWrapper.eq(PostFavour::getPostId, postId);
            postFavourQueryWrapper.eq(PostFavour::getUserId, loginUserInfoVO.getUserId());
            PostFavour postFavour = postFavourMapper.selectOne(postFavourQueryWrapper);
            postVO.setHasFavour(postFavour != null);
        }
        return postVO;
    }

}




