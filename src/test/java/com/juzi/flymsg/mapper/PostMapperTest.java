package com.juzi.flymsg.mapper;

import com.juzi.flymsg.model.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author codejuzi
 * @CreateTime 2023/4/7
 */
@SpringBootTest
class PostMapperTest {

    @Resource
    private PostMapper postMapper;

    @Test
    void postListByTags() {
        String tags = "[\"C\"]";
        tags = tags.toLowerCase(Locale.ROOT);
        List<Post> postList = postMapper.postListByTags(tags);
        assertNotNull(postList);
        System.out.println("postList = " + postList);
    }

    @Test
    void postSelectById() {
        long postId = 1;
        Post post = postMapper.postSelectById(postId);
        assertNotNull(post);
        System.out.println("post = " + post);
    }

    @Test
    void testPostListByTags() {
        String tags = "[\"java\"]";
        List<Post> postList = postMapper.postListByTags(tags);
        assertNotNull(postList);
        System.out.println("postList = " + postList);
    }

    @Test
    void postListByContentAndTags() {
        String content = "我不能";
        String tags = "[\"java\"]";
        long offset = 1;
        long pageSize = 5;
        String sortField = "id";
        List<Post> postList = postMapper.postListWithContentAndTagsByPage(content, tags, offset, pageSize, sortField, true);
        assertNotNull(postList);
        System.out.println("postList = " + postList);
        postList = postMapper.postListWithContentAndTagsByPage(content, tags,offset, pageSize,sortField, false);
        assertNotNull(postList);
        System.out.println("postList = " + postList);
    }


    @Test
    void updatePostFavours() {
        Long postId = 1L;
        Boolean updatePostFavours = postMapper.updatePostFavours(postId, 1);
        assertNotNull(updatePostFavours);
        System.out.println("updatePostFavours = " + updatePostFavours);
        updatePostFavours = postMapper.updatePostFavours(postId, -1);
        assertNotNull(updatePostFavours);
        System.out.println("updatePostFavours = " + updatePostFavours);
    }

    @Test
    void updatePostThumbs() {
        Long postId = 1L;
        boolean updatePostThumbs = postMapper.updatePostThumbs(postId, 1);
        System.out.println("updatePostThumbs = " + updatePostThumbs);
        updatePostThumbs = postMapper.updatePostThumbs(postId, -1);
        System.out.println("updatePostThumbs = " + updatePostThumbs);
    }
}