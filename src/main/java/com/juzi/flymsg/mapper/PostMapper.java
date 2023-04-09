package com.juzi.flymsg.mapper;

import com.juzi.flymsg.model.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author codejuzi
 * @description 针对表【post(帖子文章)】的数据库操作Mapper
 * @createDate 2023-04-07 20:15:18
 * @Entity com.juzi.flymsg.model.entity.Post
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 根据id查询帖子信息
     *
     * @param postId 帖子id
     * @return post
     */
    Post postSelectById(Long postId);

    /**
     * 模糊查询文章
     *
     * @param tags    文章标签
     * @return 文章列表
     */
    List<Post> postListByTags(String tags);

    /**
     * 根据内容和标签模糊查询
     *
     * @param content 内容
     * @param tags 标签
     * @return 帖子列表
     */
    List<Post> postListByContentAndTags(String content, String tags);
}




