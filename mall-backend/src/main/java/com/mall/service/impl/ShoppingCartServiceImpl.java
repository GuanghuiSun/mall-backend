package com.mall.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.mapper.ShoppingCartMapper;
import com.mall.model.domain.Product;
import com.mall.model.domain.ShoppingCart;
import com.mall.service.ProductService;
import com.mall.service.ShoppingCartService;
import java.util.Collections;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sgh
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {
    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Resource
    private ProductService productService;

    @Override
    public List<ShoppingCart> getShoppingCart(String userId) {
        return shoppingCartMapper.getShoppingCart(userId);
    }

    @Override
    public Boolean deleteShoppingCart(String userId, String productId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getProductId, productId);
        return this.remove(wrapper);
    }

    @Override
    public Boolean updateShoppingCart(String userId, String productId, Integer num) {
        //查询该商品可购买的最大值
        Integer maxPurchasableNum = productService.getMaxPurchasableNum(productId);
        if (maxPurchasableNum < num) {
            return false;
        }
        //更改购物车中的数量
        return shoppingCartMapper.updateShoppingCart(userId, productId, num);
    }

    @Override
    public Map<ShoppingCart, Integer> addShoppingCart(String userId, String productId) {
        // 首次添加0 重复添加1 已达上限-1
        Map<ShoppingCart, Integer> map = new HashMap<>();
        //查询购物车中是否有该商品
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getProductId, productId).eq(ShoppingCart::getIsPaid,0);
        ShoppingCart shoppingCart = shoppingCartMapper.selectOne(wrapper);
        //如果有则数量加一，没有则新加一个
        if (shoppingCart == null) {
            ShoppingCart newShoppingCart = new ShoppingCart();
            newShoppingCart.setUserId(Integer.parseInt(userId));
            newShoppingCart.setProductId(Integer.parseInt(productId));
            Product newProduct = productService.getById(productId);
            newShoppingCart.setProduct(newProduct);
            newShoppingCart.setNum(1);
            boolean save = this.save(newShoppingCart);
            if (save) {
                //首次添加
                map.put(newShoppingCart, 0);
                return map;
            } else {
                //添加失败
                return Collections.emptyMap();
            }
        } else {
            //如果商品已到限购数量
            Integer hadNum = shoppingCart.getNum();
            Integer maxNum = productService.getMaxPurchasableNum(productId);
            if (hadNum.equals(maxNum)) {
                //已达上限
                map.put(shoppingCart,-1);
                return map;
            }
            //更新购物车，重复添加
            this.updateShoppingCart(userId, productId, shoppingCart.getNum() + 1);
            map.put(shoppingCartMapper.selectOne(wrapper), 1);
            return map;
        }

    }

    @Override
    public Boolean updateProductStatus(String userId, String productId) {
        LambdaUpdateWrapper<ShoppingCart> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId).eq(ShoppingCart::getProductId,productId)
                .set(ShoppingCart::getIsPaid,1);
        boolean success = update(wrapper);
        return BooleanUtil.isTrue(success);
    }
}




