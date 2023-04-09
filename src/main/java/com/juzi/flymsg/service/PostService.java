package com.juzi.flymsg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juzi.flymsg.model.dto.post.PostAddRequest;
import com.juzi.flymsg.model.dto.post.PostDeleteRequest;
import com.juzi.flymsg.model.dto.post.PostSelectRequest;
import com.juzi.flymsg.model.dto.post.PostUpdateRequest;
import com.juzi.flymsg.model.entity.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author codejuzi
 * @description 针对表【post(帖子文章)】的数据库操作Service
 * @createDate 2023-04-07 20:15:18
 */
public interface PostService extends IService<Post> {

    /**
     * 新增帖子
     *
     * @param postAddRequest 帖子新增请求信息
     * @param request        request 域对象
     * @return 新增的post的id
     */
    Long postAdd(PostAddRequest postAddRequest, HttpServletRequest request);

    /**
     * 修改帖子
     *
     * @param postUpdateRequest 帖子修改请求信息
     * @param request           request 域对象
     * @return true - 修改成功
     */
    boolean postUpdate(PostUpdateRequest postUpdateRequest, HttpServletRequest request);

    /**
     * 删除帖子
     *
     * @param postDeleteRequest 帖子删除请求体信息
     * @param request           request 域对象
     * @return true - 删除成功
     */
    boolean postDelete(PostDeleteRequest postDeleteRequest, HttpServletRequest request);


    /**
     * 根据id查询帖子信息
     *
     * @param postId 帖子id
     * @return post
     */
    Post postSelectById(Long postId);

    /**
     * 查询所有帖子
     *
     * @param request request 域对象
     * @return 帖子列表
     */
    List<Post> postListAll(HttpServletRequest request);

    /**
     * 获取user的所有文章
     *
     * @param userId user id
     * @return 文章列表
     */
    List<Post> postListByUserId(Long userId);

    /**
     * 根据标题模糊查询帖子信息
     *
     * @param searchText 搜索关键词
     * @return 文章列表
     */
    List<Post> postListByTitle(String searchText);

    /**
     * 根据标签信息模糊查询
     *
     * @param tagList 标签信息
     * @return 文章列表
     */
    List<Post> postListByTags(List<String> tagList);

    /**
     * 根据内容模糊查询
     *
     * @param content 内容
     * @return 文章列表
     */
    List<Post> postListByContent(String content);

    /**
     * 根据内容和标签模糊查询
     *
     * @param postSelectRequest 帖子查询请求信息
     * @return 帖子列表
     */
    List<Post> postListByContentAndTags(PostSelectRequest postSelectRequest);
}
