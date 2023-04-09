package com.juzi.flymsg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juzi.flymsg.model.dto.postfavour.PostFavourAddRequest;
import com.juzi.flymsg.model.entity.PostFavour;
import com.juzi.flymsg.model.vo.PostVO;
import com.juzi.flymsg.model.vo.UserInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author codejuzi
 * @description 针对表【post_favour(帖子收藏)】的数据库操作Service
 * @createDate 2023-04-09 14:02:17
 */
public interface PostFavourService extends IService<PostFavour> {

    /**
     * 帖子收藏 / 取消收藏
     *
     * @param postFavourAddRequest 帖子收藏、取消收藏请求信息
     * @param loginUserInfoVO      登录用户
     * @return resultNum 收藏变化数
     */
    int doPostFavour(PostFavourAddRequest postFavourAddRequest, UserInfoVO loginUserInfoVO);

    /**
     * 执行 帖子收藏 / 取消收藏的方法（封装了事务）
     *
     * @param userId user id
     * @param postId post id
     * @return 收藏变化数
     */
    int doPostFavourInner(Long userId, Long postId);


    /**
     * 查询我收藏的文章
     *
     * @param request request 域对象
     * @return post list
     */
    List<PostVO> listMyFavourPost(HttpServletRequest request);

}
