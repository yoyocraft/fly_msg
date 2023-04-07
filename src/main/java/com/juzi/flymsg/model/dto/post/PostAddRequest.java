package com.juzi.flymsg.model.dto.post;

import lombok.Data;

import java.io.Serializable;

/**
 * @author codejuzi
 * @CreateTime 2023/4/7
 */
@Data
public class PostAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）["java", "python"]
     */
    private String tags;

    private static final long serialVersionUID = 1L;
}
