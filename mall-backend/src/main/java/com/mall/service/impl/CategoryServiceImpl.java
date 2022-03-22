package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.model.domain.Category;
import com.mall.service.CategoryService;
import com.mall.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author sgh
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Override
    public Integer getIdByName(String categoryName) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getCategoryName,categoryName);
        Category result = this.getOne(wrapper);
        if(result==null){
            return -1;
        }
        return result.getCategoryId();
    }


}




