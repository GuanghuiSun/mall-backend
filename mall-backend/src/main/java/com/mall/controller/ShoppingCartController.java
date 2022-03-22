package com.mall.controller;

import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.constant.MessageConstant;
import com.mall.model.domain.ShoppingCart;
import com.mall.model.request.ShoppingCartRequest;
import com.mall.service.ShoppingCartService;
import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionWsaServerTube;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 获取购物车所有信息
     * @param userId 用户id
     * @return
     */
    @GetMapping("/getShoppingCart")
    public BaseResponse<List<ShoppingCart>> getShoppingCart(String userId){
        if(StringUtils.isAnyBlank(userId)){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        List<ShoppingCart> shoppingCart = shoppingCartService.getShoppingCart(userId);
        if(shoppingCart==null){
            return ResultUtils.error(null, MessageConstant.SELECT_FAIL);
        }
        if(shoppingCart.isEmpty()){
            return ResultUtils.success("002",shoppingCart,MessageConstant.CART_EMPTY);
        }
        return ResultUtils.success(shoppingCart,MessageConstant.SELECT_SUCCESS);
    }

    /**
     * 删除购物车中的商品
     * @param shoppingCartRequest 购物车请求
     * @return
     */
    @PostMapping("/deleteShoppingCart")
    public BaseResponse<Boolean> deleteShoppingCart(@RequestBody ShoppingCartRequest shoppingCartRequest){
        if(shoppingCartRequest == null){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        String userId = shoppingCartRequest.getUserId();
        String productId = shoppingCartRequest.getProductId();
        if(StringUtils.isAnyBlank(userId,productId)){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        Boolean result = shoppingCartService.deleteShoppingCart(userId, productId);
        if(Boolean.TRUE.equals(result)){
            return ResultUtils.success(true,MessageConstant.DELETE_SUCCESS);
        }else{
            return ResultUtils.error(false,MessageConstant.DELETE_FAIL);
        }
    }

    /**
     * 更新购物车
     * @param shoppingCartRequest 购物车请求
     * @return
     */
    @PostMapping("/updateShoppingCart")
    public BaseResponse<Boolean> updateShoppingCart(@RequestBody ShoppingCartRequest shoppingCartRequest){
        if(shoppingCartRequest==null){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        String userId = shoppingCartRequest.getUserId();
        String productId = shoppingCartRequest.getProductId();
        Integer num = shoppingCartRequest.getNum();
        if(StringUtils.isAnyBlank(userId,productId)){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        Boolean result = shoppingCartService.updateShoppingCart(userId, productId, num);
        if(Boolean.TRUE.equals(result)){
            return ResultUtils.success(true,MessageConstant.UPDATE_SUCCESS);
        }else{
            return ResultUtils.error(false,MessageConstant.UPDATE_FAIL);
        }
    }

    /**
     * 添加购物车
     * @param shoppingCartRequest 购物车请求
     * @return
     */
    @PostMapping("/addShoppingCart")
    public BaseResponse<ShoppingCart> addShoppingCart(@RequestBody ShoppingCartRequest shoppingCartRequest){
        if(shoppingCartRequest==null){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        String userId = shoppingCartRequest.getUserId();
        String productId = shoppingCartRequest.getProductId();
        if(StringUtils.isAnyBlank(userId,productId)){
            return ResultUtils.error(null,MessageConstant.REQUEST_FAIL);
        }
        // 首次添加0 重复添加1 已达上限-1
        Map<ShoppingCart, Integer> map = shoppingCartService.addShoppingCart(userId, productId);
        if(map == null){
            //添加失败
            return ResultUtils.error(null,MessageConstant.ADD_FAIL);
        }
        Set<ShoppingCart> shoppingCarts = map.keySet();
        for(ShoppingCart shoppingCart : shoppingCarts){
            Integer value = map.get(shoppingCart);
            switch (value) {
                case 0:
                    //新加入
                    return ResultUtils.success(shoppingCart,MessageConstant.ADD_SUCCESS);
                case 1:
                    //多次加入
                    return ResultUtils.success("002",shoppingCart,MessageConstant.ADD_SUCCESS);
                case -1:
                    //已达上限
                    return ResultUtils.error("003",shoppingCart,MessageConstant.ADD_LIMIT_FAIL);
                default:
                    return ResultUtils.error(null,MessageConstant.ADD_FAIL);
            }
        }
        return ResultUtils.error(null,MessageConstant.ADD_FAIL);
    }


}
