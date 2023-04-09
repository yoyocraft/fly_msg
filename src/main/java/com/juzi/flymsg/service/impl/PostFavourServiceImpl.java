package com.juzi.flymsg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.exception.BusinessException;
import com.juzi.flymsg.manager.UserManager;
import com.juzi.flymsg.mapper.PostFavourMapper;
import com.juzi.flymsg.mapper.PostMapper;
import com.juzi.flymsg.mapper.PostThumbMapper;
import com.juzi.flymsg.model.dto.postfavour.PostFavourAddRequest;
import com.juzi.flymsg.model.entity.Post;
import com.juzi.flymsg.model.entity.PostFavour;
import com.juzi.flymsg.model.vo.PostVO;
import com.juzi.flymsg.model.vo.UserInfoVO;
import com.juzi.flymsg.model.vo.UserVO;
import com.juzi.flymsg.service.PostFavourService;
import com.juzi.flymsg.service.PostService;
import com.juzi.flymsg.service.UserInfoService;
import com.juzi.flymsg.utils.ThrowUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author codejuzi
 * @description 针对表【post_favour(帖子收藏)】的数据库操作Service实现
 * @createDate 2023-04-09 14:02:17
 */
@Service
public class PostFavourServiceImpl extends ServiceImpl<PostFavourMapper, PostFavour>
        implements PostFavourService {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private PostService postService;

    @Resource
    private UserManager userManager;

    @Resource
    private PostMapper postMapper;

    @Resource
    private PostThumbMapper postThumbMapper;

    @Override
    public int doPostFavour(PostFavourAddRequest postFavourAddRequest, UserInfoVO loginUserInfoVO) {
        // 判断帖子存在
        Long postId = postFavourAddRequest.getPostId();
        Post post = postMapper.selectById(postId);
        ThrowUtil.throwIf(post == null, ErrorCode.NOT_FOUND);
        // 每个用户必须串行执行 => 事务失效的几种方式
        Long userId = loginUserInfoVO.getUserId();
        PostFavourService postFavourService = (PostFavourService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return postFavourService.doPostFavourInner(userId, postId);
        }
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public int doPostFavourInner(Long userId, Long postId) {
        PostFavour postFavour = new PostFavour();
        postFavour.setPostId(postId);
        postFavour.setUserId(userId);
        QueryWrapper<PostFavour> queryWrapper = new QueryWrapper<>(postFavour);
        PostFavour oldPostFavour = this.getOne(queryWrapper);
        boolean result;
        // 已收藏
        if (oldPostFavour != null) {
            // 删除帖子收藏表
            result = this.remove(queryWrapper);
            ThrowUtil.throwIf(!result, ErrorCode.SYSTEM_ERROR);
            // 帖子表的收藏数 - 1
            result = postMapper.updatePostFavours(postId, -1);
            return result ? -1 : 0;
        }
        // 未收藏
        else {
            // 新增帖子收藏表
            result = this.save(postFavour);
            ThrowUtil.throwIf(!result, ErrorCode.SYSTEM_ERROR);
            // 帖子表的收藏数 + 1
            result = postMapper.updatePostFavours(postId, 1);
            return result ? 1 : 0;
        }
    }

    @Override
    public List<PostVO> listMyFavourPost(HttpServletRequest request) {
        // 获取当前登录用户
        UserInfoVO loginUser = userManager.getLoginUser(request);
        UserVO userVO = userInfoService.getUserVO(loginUser);
        Long userId = loginUser.getUserId();
        LambdaQueryWrapper<PostFavour> postFavourLambdaQueryWrapper = new LambdaQueryWrapper<>();
        postFavourLambdaQueryWrapper.eq(PostFavour::getUserId, userId);
        // 得到当前用户点赞帖子过的帖子id
        List<Long> postFavourIdList = this.list(postFavourLambdaQueryWrapper)
                .stream()
                .map(PostFavour::getPostId)
                .collect(Collectors.toList());
        // 查询并封装
        return postFavourIdList.stream()
                .map(postId -> {
                    Post post = postMapper.selectById(postId);
                    PostVO postVO = PostVO.objToVo(post);
                    postVO.setUser(userVO);
                    postVO.setHasFavour(true);
                    int count = postThumbMapper.hasThumbed(postId, userId);
                    postVO.setHasThumb(count > 0);
                    return postVO;
                })
                .collect(Collectors.toList());
    }
}




