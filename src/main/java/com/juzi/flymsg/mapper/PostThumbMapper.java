package com.juzi.flymsg.mapper;

import com.juzi.flymsg.model.entity.PostThumb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author codejuzi
 * @description 针对表【post_thumb(帖子点赞)】的数据库操作Mapper
 * @createDate 2023-04-09 14:02:26
 * @Entity com.juzi.flymsg.model.entity.PostThumb
 */
@Mapper
public interface PostThumbMapper extends BaseMapper<PostThumb> {

    /**
     * 查询post是否被点赞过
     *
     * @param postId post id
     * @param userId user id
     * @return count > 0 => 点赞过
     */
    int hasThumbed(Long postId, Long userId);
}




