package com.mall.service;

import com.mall.model.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author sgh
*/
public interface CategoryService extends IService<Category> {

    /**
     * 根据分类名获取分类id
     * @param categoryName 分类名
     * @return
     */
    Integer getIdByName(String categoryName);
}
