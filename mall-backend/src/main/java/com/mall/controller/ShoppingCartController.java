package com.mall.controller;

import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.exception.BusinessException;
import com.mall.model.domain.ShoppingCart;
import com.mall.model.request.ShoppingCartRequest;
import com.mall.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mall.base.ErrorCode.*;
import static com.mall.constant.MessageConstant.*;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 获取购物车所有信息
     * @param userId 用户id
     * @return 购物车所有信息
     */
    @GetMapping("/getShoppingCart")
    public BaseResponse<List<ShoppingCart>> getShoppingCart(String userId){
        if(StringUtils.isAnyBlank(userId)){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        List<ShoppingCart> shoppingCart = shoppingCartService.getShoppingCart(userId);
        log.info(String.valueOf(shoppingCart));
        return ResultUtils.success(shoppingCart,SELECT_SUCCESS);
    }

    /**
     * 删除购物车中的商品
     * @param shoppingCartRequest 购物车请求
     * @return 是否成功
     */
    @PostMapping("/deleteShoppingCart")
    public BaseResponse<Boolean> deleteShoppingCart(@RequestBody ShoppingCartRequest shoppingCartRequest){
        if(shoppingCartRequest == null){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        String userId = shoppingCartRequest.getUserId();
        String productId = shoppingCartRequest.getProductId();
        if(StringUtils.isAnyBlank(userId,productId)){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        Boolean result = shoppingCartService.deleteShoppingCart(userId, productId);
        if(Boolean.TRUE.equals(result)){
            return ResultUtils.success(true,DELETE_SUCCESS);
        }else{
            throw new BusinessException(REQUEST_SERVICE_ERROR,DELETE_FAIL);
        }
    }

    /**
     * 更新购物车
     * @param shoppingCartRequest 购物车请求
     * @return 是否成功
     */
    @PostMapping("/updateShoppingCart")
    public BaseResponse<Boolean> updateShoppingCart(@RequestBody ShoppingCartRequest shoppingCartRequest){
        if(shoppingCartRequest==null){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        String userId = shoppingCartRequest.getUserId();
        String productId = shoppingCartRequest.getProductId();
        Integer num = shoppingCartRequest.getNum();
        if(StringUtils.isAnyBlank(userId,productId)){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        Boolean result = shoppingCartService.updateShoppingCart(userId, productId, num);
        if(Boolean.TRUE.equals(result)){
            return ResultUtils.success(true,UPDATE_SUCCESS);
        }else{
            throw new BusinessException(REQUEST_SERVICE_ERROR,UPDATE_FAIL);
        }
    }

    /**
     * 添加购物车
     * @param shoppingCartRequest 购物车请求
     * @return 购物车
     */
    @PostMapping("/addShoppingCart")
    public BaseResponse<ShoppingCart> addShoppingCart(@RequestBody ShoppingCartRequest shoppingCartRequest){
        if(shoppingCartRequest==null){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        String userId = shoppingCartRequest.getUserId();
        String productId = shoppingCartRequest.getProductId();
        if(StringUtils.isAnyBlank(userId,productId)){
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        // 首次添加0 重复添加1 已达上限-1
        Map<ShoppingCart, Integer> map = shoppingCartService.addShoppingCart(userId, productId);
        if(map == null || map.isEmpty()) {
            //添加失败
            throw new BusinessException(REQUEST_SERVICE_ERROR,ADD_FAIL);
        }
        Set<ShoppingCart> shoppingCarts = map.keySet();
        for(ShoppingCart shoppingCart : shoppingCarts){
            Integer value = map.get(shoppingCart);
            switch (value) {
                case 0:
                    //新加入
                    return ResultUtils.success(shoppingCart,ADD_SUCCESS);
                case 1:
                    //多次加入
                    return ResultUtils.success(202,shoppingCart,ADD_SUCCESS);
                case -1:
                    //已达上限
                    return ResultUtils.error(203,shoppingCart,ADD_LIMIT_FAIL);
                default:
                    throw new BusinessException(REQUEST_SERVICE_ERROR,ADD_FAIL);
            }
        }
        throw new BusinessException(REQUEST_SERVICE_ERROR,ADD_FAIL);
    }
}
