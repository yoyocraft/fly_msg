package com.juzi.flymsg.model.dto.postfavour;

import lombok.Data;

import java.io.Serializable;

/**
 * @author codejuzi
 * @CreateTime 2023/4/9
 */
@Data
public class PostFavourAddRequest implements Serializable {

    private static final long serialVersionUID = 2065848042574352774L;

    /**
     * 帖子id
     */
    private Long postId;
}
