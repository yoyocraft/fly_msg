package com.juzi.flymsg.service;

import com.juzi.flymsg.model.dto.postthumb.PostThumbAddRequest;
import com.juzi.flymsg.model.entity.PostThumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juzi.flymsg.model.vo.PostVO;
import com.juzi.flymsg.model.vo.UserInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author codejuzi
 * @description 针对表【post_thumb(帖子点赞)】的数据库操作Service
 * @createDate 2023-04-09 14:02:26
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 帖子点赞 / 取消点赞
     *
     * @param postThumbAddRequest 帖子点赞、取消点赞请求信息
     * @param loginUserInfoVO     当前登录用户
     * @return resultNum 点赞变化数
     */
    int doPostThumb(PostThumbAddRequest postThumbAddRequest, UserInfoVO loginUserInfoVO);

    /**
     * 执行 帖子点赞 / 取消点赞的方法（封装了事务）
     *
     * @param userId user id
     * @param postId post id
     * @return 点赞变化数
     */
    int doPostFavourInner(Long userId, Long postId);

    /**
     * 查询我点赞的文章
     *
     * @param request request 请求域
     * @return post list
     */
    List<PostVO> listMyThumbPost(HttpServletRequest request);
}
