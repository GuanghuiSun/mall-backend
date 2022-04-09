package com.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.exception.BusinessException;
import com.mall.model.domain.Category;
import com.mall.model.domain.Product;
import com.mall.model.domain.ProductPicture;
import com.mall.model.request.ProductPageRequest;
import com.mall.model.request.ProductPictureRequest;
import com.mall.service.CategoryService;
import com.mall.service.ProductPictureService;
import com.mall.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.mall.base.ErrorCode.*;
import static com.mall.constant.MessageConstant.*;

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
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        List<Product> productByCategory = productService.getProductByCategoryName(categoryName);
        if (productByCategory == null) {
            throw new BusinessException(GET_MESSAGE_ERROR,SELECT_FAIL);
        }
        return ResultUtils.success(productByCategory, SELECT_SUCCESS);
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
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        List<Product> hotProduct = productService.getHotProduct(categoryName);
        return ResultUtils.success(hotProduct, SELECT_SUCCESS);
    }

    /**
     * 获取所有分类
     * @return
     */
    @GetMapping("/getCategory")
    public BaseResponse<List<Category>> getCategory(){
        List<Category> categoryList = categoryService.getCategoryList();
        return ResultUtils.success(categoryList, SELECT_SUCCESS);
    }

    /**
     * 分页获取所有商品
     * @param productPageRequest 分页获取商品信息请求
     * @return
     */
    @PostMapping("/getAllProduct")
    public BaseResponse<IPage<Product>> getAllProduct(@RequestBody ProductPageRequest productPageRequest){
        if(productPageRequest == null){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        int pageSize = productPageRequest.getPageSize();
        int currentPage = productPageRequest.getCurrentPage();
        IPage<Product> products = productService.getALlProduct(currentPage, pageSize);
        if(products == null){
            throw new BusinessException(GET_MESSAGE_ERROR,SELECT_FAIL);
        }
        return ResultUtils.success(products,SELECT_SUCCESS);
    }

    /**
     * 根据分类分页获取所有商品信息
     * @param productPageRequest 分页分类获取商品信息请求
     * @return
     */
    @PostMapping("/getProductByCategory")
    public BaseResponse<IPage<Product>> getProductByCategory(@RequestBody ProductPageRequest productPageRequest){
        if(productPageRequest == null){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        String[] category = productPageRequest.getCategory();
        int currentPage = productPageRequest.getCurrentPage();
        int pageSize = productPageRequest.getPageSize();
        IPage<Product> products = productService.getProductByCategory(Integer.parseInt(category[0]), currentPage, pageSize);
        if(products == null){
            throw new BusinessException(GET_MESSAGE_ERROR,SELECT_FAIL);
        }
        return ResultUtils.success(products,SELECT_SUCCESS);
    }

    /**
     * 根据搜索内容获取分类分页商品信息
     * @param productPageRequest 分页搜素查询获取商品信息请求
     * @return
     */
    @PostMapping("/getProductBySearch")
    public BaseResponse<IPage<Product>> getProductBySearch(@RequestBody ProductPageRequest productPageRequest){
        if(productPageRequest == null){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        String queryString = productPageRequest.getSearch();
        int currentPage = productPageRequest.getCurrentPage();
        int pageSize = productPageRequest.getPageSize();
        IPage<Product> products = productService.getProductBySearch(queryString, currentPage, pageSize);
        if(products == null) {
            throw new BusinessException(GET_MESSAGE_ERROR,SELECT_FAIL);
        }
        return ResultUtils.success(products,SELECT_SUCCESS);
    }

    /**
     * 获取商品的详细信息
     * @param productId 商品id
     * @return
     */
    @GetMapping("/getDetails")
    public BaseResponse<Product> getDetailsById(String productId){
        if(productId == null){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        Product product = productService.getById(productId);
        if(product == null){
            throw new BusinessException(GET_MESSAGE_ERROR,SELECT_FAIL);
        }
        return ResultUtils.success(product,SELECT_SUCCESS);
    }

    /**
     * 获取商品的所有图片
     * @param productId 商品id
     * @return
     */
    @GetMapping("/getDetailsPicture")
    public BaseResponse<List<ProductPicture>> getDetailsPicture(String productId){
        if(productId == null){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        List<ProductPicture> detailsPicture = productPictureService.getDetailsPicture(productId);
        if(detailsPicture == null){
            throw new BusinessException(GET_MESSAGE_ERROR,SELECT_FAIL);
        }
        return ResultUtils.success(detailsPicture,SELECT_SUCCESS);
    }

    /**
     * 添加商品
     * @param product 商品
     * @return
     */
    @PostMapping("/addProduct")
    public BaseResponse<Integer> addProduct(@RequestBody Product product) {
        //判断商品是否为空
        if(product==null){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        Integer productId = productService.addProduct(product);
        return ResultUtils.success(productId, ADD_SUCCESS);

    }

    /**
     * 添加图片
     * @param productPictureRequest 请求体
     * @return
     */
    @PostMapping("/addPictures")
    public BaseResponse<Boolean> addProductPictures(@RequestBody ProductPictureRequest productPictureRequest) {
        if(productPictureRequest == null) {
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        Integer productId = productPictureRequest.getProductId();
        String[] pictures = productPictureRequest.getPictures();
        String pictureIntro = productPictureRequest.getPictureIntro();
        Boolean success = productService.addProductPictures(pictures, productId, pictureIntro);
        return ResultUtils.success(success,ADD_SUCCESS);
    }

    /**
     * 删除商品的一些详细图片
     * @param productPictureRequest 商品图片请求体
     * @return
     */
    @DeleteMapping("/removePictures")
    public BaseResponse<Boolean> removeProductPictures(@RequestBody ProductPictureRequest productPictureRequest) {
        if (productPictureRequest == null) {
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        Integer productId = productPictureRequest.getProductId();
        String[] pictures = productPictureRequest.getPictures();
        Boolean success = productService.removeProductPictures(pictures, productId);
        return ResultUtils.success(success, DELETE_SUCCESS);
    }


    /**
     * 删除商品
     * @param productId 商品id
     * @return
     */
    @DeleteMapping("/{productId}")
    public BaseResponse<Boolean> deleteProduct(@PathVariable("productId") Integer productId) {
        if(productId==null) {
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        Boolean success = productService.deleteProduct(productId);
        return ResultUtils.success(success, DELETE_SUCCESS);
    }

    /**
     * 更新商品
     * @param product 商品
     * @return
     */
    @PutMapping
    public BaseResponse<Boolean> updateProduct(@RequestBody Product product) {
        if(product==null) {
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        Boolean success = productService.updateProduct(product);
        return ResultUtils.success(success, UPDATE_SUCCESS);
    }

}
