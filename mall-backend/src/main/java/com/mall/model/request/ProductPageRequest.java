package com.mall.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取分页查询请求
 */
@Data
public class ProductPageRequest implements Serializable {
    private static final long serialVersionUID = -3415777259057739752L;
    private String[] category;
    private String search;
    private int currentPage;
    private int pageSize;
}
