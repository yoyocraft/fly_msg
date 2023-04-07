package com.juzi.flymsg.model.dto.post;

import lombok.Data;

import java.util.List;

/**
 * 帖子模糊查询请求体
 *
 * @author codejuzi
 * @CreateTime 2023/4/7
 */
@Data
public class PostSelectRequest {

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 内容
     */
    private String content;

}
