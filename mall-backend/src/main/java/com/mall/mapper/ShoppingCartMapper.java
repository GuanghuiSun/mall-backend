package com.mall.mapper;

import com.mall.model.domain.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
*@author sgh
*/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    /**
     * 获取购物车
     * @param userId 用户id
     * @return
     */
    List<ShoppingCart> getShoppingCart(String userId);

    /**
     * 更新购物车
     * @param userId 用户id
     * @param productId 商品id
     * @param num 数量
     * @return
     */
    @Update("update shoppingcart set num = #{num} where user_id = #{userId} and product_id = #{productId}")
    Boolean updateShoppingCart(String userId,String productId,Integer num);
}




