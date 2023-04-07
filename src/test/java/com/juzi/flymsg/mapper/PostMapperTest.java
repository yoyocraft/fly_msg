package com.juzi.flymsg.mapper;

import com.juzi.flymsg.model.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

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
        System.out.println(postList);
    }
}