package com.mall.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.model.domain.Category;
import com.mall.service.CategoryService;
import com.mall.mapper.CategoryMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.mall.constant.RedisConstant.CACHE_CATEGORY_KEY;

/**
* @author sgh
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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

    @Override
    public List<Category> getCategoryList() {
        //从redis缓存中获取分类信息                  从左到右
        List<String> category = stringRedisTemplate.opsForList().range(CACHE_CATEGORY_KEY, 0, -1);
        if(category != null && !category.isEmpty()){
            return category.stream()
                    .map(jsonString -> JSONUtil.toBean(jsonString, Category.class))
                    .collect(Collectors.toList());
        }
        //没有查询到缓存，去查数据库
        List<Category> list = this.list();
        //写入缓存
        List<String> collect = list.stream().map(JSONUtil::toJsonStr).collect(Collectors.toList());
        //右边插入所有种类信息
        stringRedisTemplate.opsForList().rightPushAll(CACHE_CATEGORY_KEY, collect);
        return list;
    }


}




