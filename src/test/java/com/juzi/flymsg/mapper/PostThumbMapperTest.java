package com.juzi.flymsg.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author codejuzi
 * @CreateTime 2023/4/9
 */
@SpringBootTest
class PostThumbMapperTest {

    @Resource
    private PostThumbMapper postThumbMapper;

    @Test
    void hasThumbed() {
        long postId = 1L;
        long userId = 6L;
        int hasThumbed = postThumbMapper.hasThumbed(postId, userId);
        System.out.println("hasThumbed = " + hasThumbed);
    }
}