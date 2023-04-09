package com.juzi.flymsg.model.dto.post;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子模糊查询请求体
 *
 * @author codejuzi
 * @CreateTime 2023/4/7
 */
@Data
public class PostSelectRequest implements Serializable {

    private static final long serialVersionUID = -7529385723828905523L;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tagList;

}
