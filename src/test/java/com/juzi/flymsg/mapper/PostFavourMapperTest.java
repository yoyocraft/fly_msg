package com.juzi.flymsg.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author codejuzi
 * @CreateTime 2023/4/9
 */
@SpringBootTest
class PostFavourMapperTest {

    @Resource
    private PostFavourMapper postFavourMapper;

    @Test
    void hasFavoured() {
        long postId = 1;
        long userId = 6;
        int count = postFavourMapper.hasFavoured(postId, userId);
        System.out.println("count = " + count);
        assertNotEquals(0, count);
    }
}