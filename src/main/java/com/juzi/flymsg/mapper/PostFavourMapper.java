package com.juzi.flymsg.mapper;

import com.juzi.flymsg.model.entity.PostFavour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author codejuzi
 * @description 针对表【post_favour(帖子收藏)】的数据库操作Mapper
 * @createDate 2023-04-09 14:02:17
 * @Entity com.juzi.flymsg.model.entity.PostFavour
 */
@Mapper
public interface PostFavourMapper extends BaseMapper<PostFavour> {

    /**
     * 查询post是否被收藏过
     *
     * @param postId post id
     * @param userId user id
     * @return count > 0 => 收藏过
     */
    int hasFavoured(long postId, long userId);
}




