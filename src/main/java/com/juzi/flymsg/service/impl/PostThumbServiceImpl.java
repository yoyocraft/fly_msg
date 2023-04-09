package com.juzi.flymsg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.flymsg.common.ErrorCode;
import com.juzi.flymsg.manager.UserManager;
import com.juzi.flymsg.mapper.PostFavourMapper;
import com.juzi.flymsg.mapper.PostMapper;
import com.juzi.flymsg.mapper.PostThumbMapper;
import com.juzi.flymsg.model.dto.postthumb.PostThumbAddRequest;
import com.juzi.flymsg.model.entity.Post;
import com.juzi.flymsg.model.entity.PostThumb;
import com.juzi.flymsg.model.vo.PostVO;
import com.juzi.flymsg.model.vo.UserInfoVO;
import com.juzi.flymsg.model.vo.UserVO;
import com.juzi.flymsg.service.PostThumbService;
import com.juzi.flymsg.utils.ThrowUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author codejuzi
 * @description 针对表【post_thumb(帖子点赞)】的数据库操作Service实现
 * @createDate 2023-04-09 14:02:26
 */
@Service
public class PostThumbServiceImpl extends ServiceImpl<PostThumbMapper, PostThumb>
        implements PostThumbService {

    @Resource
    private PostFavourMapper postFavourMapper;

    @Resource
    private UserManager userManager;

    @Resource
    private PostMapper postMapper;

    @Override
    public int doPostThumb(PostThumbAddRequest postThumbAddRequest, UserInfoVO loginUserInfoVO) {
        // 判断帖子存在
        Long postId = postThumbAddRequest.getPostId();
        Post post = postMapper.selectById(postId);
        ThrowUtil.throwIf(post == null, ErrorCode.NOT_FOUND);
        // 每个用户必须串行执行 => 事务失效的几种方式
        Long userId = loginUserInfoVO.getUserId();
        PostThumbService postThumbService = (PostThumbService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return postThumbService.doPostThumbInner(userId, postId);
        }
    }

    @Override
    public int doPostThumbInner(Long userId, Long postId) {
        PostThumb postThumb = new PostThumb();
        postThumb.setUserId(userId);
        postThumb.setPostId(postId);
        QueryWrapper<PostThumb> thumbQueryWrapper = new QueryWrapper<>(postThumb);
        PostThumb oldPostThumb = this.getOne(thumbQueryWrapper);
        boolean result;
        if (oldPostThumb != null) {
            // 已点赞
            result = this.remove(thumbQueryWrapper);
            ThrowUtil.throwIf(!result, ErrorCode.SYSTEM_ERROR);
            result = postMapper.updatePostThumbs(postId, -1);
            return result ? -1 : 0;
        } else {
            // 未点赞
            result = this.save(postThumb);
            ThrowUtil.throwIf(!result, ErrorCode.SYSTEM_ERROR);
            result = postMapper.updatePostThumbs(postId, 1);
            return result ? 1 : 0;
        }
    }

    @Override
    public List<PostVO> listMyThumbPost(HttpServletRequest request) {

        // 获取当前登录用户
        UserInfoVO loginUser = userManager.getLoginUser(request);
        UserVO userVO = userManager.getUserVO(loginUser);

        Long userId = loginUser.getUserId();
        LambdaQueryWrapper<PostThumb> postThumbLambdaQueryWrapper = new LambdaQueryWrapper<>();
        postThumbLambdaQueryWrapper.eq(PostThumb::getUserId, userId);
        // 得到当前用户点赞帖子过的帖子id
        List<Long> postThumbIdList = this.list(postThumbLambdaQueryWrapper)
                .stream()
                .map(PostThumb::getPostId)
                .collect(Collectors.toList());
        // 查询并封装
        return postThumbIdList.stream()
                .map(postId -> {
                    Post post = postMapper.selectById(postId);
                    PostVO postVO = PostVO.objToVo(post);
                    postVO.setUser(userVO);
                    postVO.setHasThumb(true);
                    // 查询是否已收藏
                    int count = postFavourMapper.hasFavoured(postId, userId);
                    postVO.setHasFavour(count > 0);
                    return postVO;
                })
                .collect(Collectors.toList());

    }


}




