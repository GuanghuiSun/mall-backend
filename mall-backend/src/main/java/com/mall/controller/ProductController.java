package com.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.constant.MessageConstant;
import com.mall.model.domain.Category;
import com.mall.model.domain.Product;
import com.mall.model.domain.ProductPicture;
import com.mall.model.request.ProductPageRequest;
import com.mall.service.CategoryService;
import com.mall.service.ProductPictureService;
import com.mall.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * product表现层
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ProductPictureService productPictureService;

    /**
     * 获取分类下的商品信息
     *
     * @param categoryName 分类名
     * @return
     */
    @GetMapping("/getPromoProduct")
    public BaseResponse<List<Product>> getPromoProduct(String categoryName) {
        if (categoryName == null) {
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        List<Product> productByCategory = productService.getProductByCategoryName(categoryName);
        if (productByCategory == null) {
            return ResultUtils.error(null, MessageConstant.SELECT_FAIL);
        }
        return ResultUtils.success(productByCategory, MessageConstant.SELECT_SUCCESS);
    }

    /**
     * 获取热门商品
     *
     * @param categoryName 分类名
     * @return
     */
    @GetMapping("/getHotProduct")
    public BaseResponse<List<Product>> getHotProduct(String[] categoryName) {
        if (categoryName == null) {
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        List<Product> hotProduct = productService.getHotProduct(categoryName);
        if (hotProduct == null) {
            return ResultUtils.error(null, MessageConstant.SELECT_FAIL);
        }
        return ResultUtils.success(hotProduct, MessageConstant.SELECT_SUCCESS);
    }

    /**
     * 获取所有分类
     * @return
     */
    @GetMapping("/getCategory")
    public BaseResponse<List<Category>> getCategory(){
        List<Category> categoryList = categoryService.getCategoryList();
        if(categoryList == null){
            return ResultUtils.error(null, MessageConstant.SELECT_FAIL);
        }
        return ResultUtils.success(categoryList, MessageConstant.SELECT_SUCCESS);
    }

    /**
     * 分页获取所有商品
     * @param productPageRequest 分页获取商品信息请求
     * @return
     */
    @PostMapping("/getAllProduct")
    public BaseResponse<IPage<Product>> getAllProduct(@RequestBody ProductPageRequest productPageRequest){
        if(productPageRequest == null){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        int pageSize = productPageRequest.getPageSize();
        int currentPage = productPageRequest.getCurrentPage();
        IPage<Product> products = productService.getALlProduct(currentPage, pageSize);
        if(products == null){
            return ResultUtils.error(null,MessageConstant.SELECT_FAIL);
        }
        return ResultUtils.success(products,MessageConstant.SELECT_SUCCESS);
    }

    /**
     * 根据分类分页获取所有商品信息
     * @param productPageRequest 分页分类获取商品信息请求
     * @return
     */
    @PostMapping("/getProductByCategory")
    public BaseResponse<IPage<Product>> getProductByCategory(@RequestBody ProductPageRequest productPageRequest){
        if(productPageRequest == null){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        String[] category = productPageRequest.getCategory();
        int currentPage = productPageRequest.getCurrentPage();
        int pageSize = productPageRequest.getPageSize();
        IPage<Product> products = productService.getProductByCategory(Integer.parseInt(category[0]), currentPage, pageSize);
        if(products == null){
            return ResultUtils.error(null,MessageConstant.SELECT_FAIL);
        }
        return ResultUtils.success(products,MessageConstant.SELECT_SUCCESS);
    }

    /**
     * 根据搜索内容获取分类分页商品信息
     * @param productPageRequest 分页搜素查询获取商品信息请求
     * @return
     */
    @PostMapping("/getProductBySearch")
    public BaseResponse<IPage<Product>> getProductBySearch(@RequestBody ProductPageRequest productPageRequest){
        if(productPageRequest == null){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        String queryString = productPageRequest.getSearch();
        int currentPage = productPageRequest.getCurrentPage();
        int pageSize = productPageRequest.getPageSize();
        IPage<Product> products = productService.getProductBySearch(queryString, currentPage, pageSize);
        if(products == null) {
            return ResultUtils.error(null, MessageConstant.SELECT_FAIL);
        }
        return ResultUtils.success(products,MessageConstant.SELECT_SUCCESS);
    }

    /**
     * 获取商品的详细信息
     * @param productId 商品id
     * @return
     */
    @GetMapping("/getDetails")
    public BaseResponse<Product> getDetailsById(String productId){
        if(productId == null){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        Product product = productService.getById(productId);
        if(product == null){
            return ResultUtils.error(null, MessageConstant.SELECT_FAIL);
        }
        return ResultUtils.success(product,MessageConstant.SELECT_SUCCESS);
    }

    /**
     * 获取商品的所有图片
     * @param productId 商品id
     * @return
     */
    @GetMapping("/getDetailsPicture")
    public BaseResponse<List<ProductPicture>> getDetailsPicture(String productId){
        if(productId == null){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        List<ProductPicture> detailsPicture = productPictureService.getDetailsPicture(productId);
        if(detailsPicture == null){
            return ResultUtils.error(null, MessageConstant.SELECT_FAIL);
        }
        return ResultUtils.success(detailsPicture,MessageConstant.SELECT_SUCCESS);
    }

}
