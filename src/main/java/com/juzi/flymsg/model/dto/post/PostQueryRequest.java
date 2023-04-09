package com.juzi.flymsg.model.dto.post;

import com.juzi.flymsg.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子模糊查询请求体
 *
 * @author codejuzi
 * @CreateTime 2023/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostQueryRequest extends PageRequest implements Serializable {

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
