package com.juzi.flymsg.common;

import com.juzi.flymsg.constant.CommonConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求
 *
 * @author codejuzi
 * @CreateTime 2023/4/9
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 9048143883756480530L;

    /**
     * 当前页数
     */
    private int pageNum = 1;

    /**
     * 每页显示记录数
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
