package com.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.model.domain.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author sgh
 */
public interface ProductService extends IService<Product> {

    /**
     * 获取指定商品的可购买最大值
     *
     * @param productId 商品id
     * @return
     */
    Integer getMaxPurchasableNum(String productId);

    /**
     * 根据分类及销量获取商品信息前七个
     *
     * @param categoryName 分类名称
     * @return
     */
    List<Product> getProductByCategoryName(String categoryName);

    /**
     * 根据分类及销量获取热门商品前七个
     *
     * @param categoryNames 分类名
     * @return
     */
    List<Product> getHotProduct(String[] categoryNames);

    /**
     * 分页获取所有商品信息
     *
     * @param currentPage 当前页
     * @param pageSize    每页数量
     * @return
     */
    IPage<Product> getALlProduct(int currentPage, int pageSize);

    /**
     * 根据分类分页获取所有商品信息
     *
     * @param categoryId  分类号
     * @param currentPage 当前页
     * @param pageSize    每页数量
     * @return
     */
    IPage<Product> getProductByCategory(Integer categoryId, int currentPage, int pageSize);

    /**
     * 根据搜索信息及分类分页获取所有商品信息
     *
     * @param queryString 搜索信息 可以是name title introduce
     * @param currentPage 当前页面
     * @param pageSize    每页数量
     * @return
     */
    IPage<Product> getProductBySearch(String queryString, int currentPage, int pageSize);

    /**
     * 添加商品
     *
     * @param product 商品
     * @return
     */
    Integer addProduct(Product product);

    /**
     * 添加图片
     *
     * @param pictures 商品图片
     * @return
     */
    Boolean addProductPictures(String[] pictures, Integer productId, String pictureIntro);

    /**
     * 删除商品
     *
     * @param productId 商品id
     * @return
     */
    Boolean deleteProduct(Integer productId);

    /**
     * 删除商品图片
     *
     * @param pictures  图片
     * @param productId 商品id
     * @return
     */
    Boolean removeProductPictures(String[] pictures, Integer productId);

    /**
     * 更新商品
     *
     * @param product 商品
     * @return
     */
    Boolean updateProduct(Product product);

    /**
     * 根据分类号获取商品
     *
     * @param categoryList 分类号列表
     * @return
     */
    List<Product> getProductByCategoryIds(Integer[] categoryList);
}
