package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mapper.CategoryMapper;
import com.mall.model.domain.Product;
import com.mall.mapper.ProductMapper;
import com.mall.service.CategoryService;
import com.mall.service.ProductService;
import lombok.val;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author sgh
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
        implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CategoryService categoryService;

    @Override
    public Integer getMaxPurchasableNum(String productId) {
        Product product = this.getById(productId);
        int maxPurchasableNum = product.getProductNum() - product.getProductSales();
        if (maxPurchasableNum <= 0) {
            return 0;
        } else {
            return maxPurchasableNum;
        }
    }

    @Override
    public List<Product> getProductByCategoryName(String categoryName) {
        //根据分类名获取分类号
        Integer categoryId = categoryService.getIdByName(categoryName);
        //根据分类号获取商品
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCategoryId, categoryId)
                .orderByDesc(Product::getProductSales).last("LIMIT 7");
        return productMapper.selectList(wrapper);
    }

    @Override
    public List<Product> getHotProduct(String[] categoryNames) {
        //根据分类名获取分类号
        Integer[] categoryIds = new Integer[categoryNames.length];
        for (int i = 0; i < categoryNames.length; i++) {
            Integer categoryId = categoryService.getIdByName(categoryNames[i]);
            categoryIds[i] = categoryId;
        }
        //根据分类号获取销量前7名
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Product::getCategoryId, Arrays.asList(categoryIds))//分类号查询
                .orderByDesc(Product::getProductSales).last("LIMIT 7");
        return productMapper.selectList(wrapper);
    }

    @Override
    public IPage<Product> getALlProduct(int currentPage, int pageSize) {
        IPage<Product> page = new Page<>(currentPage, pageSize);
        return productMapper.selectPage(page, null);
    }

    @Override
    public IPage<Product> getProductByCategory(Integer categoryId, int currentPage, int pageSize) {
        IPage<Product> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCategoryId, categoryId);
        return productMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<Product> getProductBySearch(String queryString, int currentPage, int pageSize) {
        IPage<Product> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Product::getProductName, queryString)
                .or().like(Product::getProductTitle, queryString)
                .or().like(Product::getProductIntro, queryString);
        return productMapper.selectPage(page, wrapper);
    }



}




