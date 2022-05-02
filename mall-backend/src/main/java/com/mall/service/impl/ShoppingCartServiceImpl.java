package com.mall.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.exception.BusinessException;
import com.mall.mapper.ShoppingCartMapper;
import com.mall.model.domain.Product;
import com.mall.model.domain.ShoppingCart;
import com.mall.service.ProductService;
import com.mall.service.ShoppingCartService;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.mall.base.ErrorCode.REQUEST_SERVICE_ERROR;
import static com.mall.constant.MessageConstant.INVENTORY_SHORTAGE_ERROR;
import static com.mall.constant.MessageConstant.UPDATE_FAIL;
import static com.mall.constant.RedisConstant.SHOPPING_CART_KEY;

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

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<ShoppingCart> getShoppingCart(String userId) {
        // 从redis中读取购物车
        String key = SHOPPING_CART_KEY + ":" + userId;
        List<Object> values = stringRedisTemplate.opsForHash().values(key);
        if (!values.isEmpty()) {
            //缓存中购物车不为空
            List<ShoppingCart> shoppingCart = values.stream()
                    .map(jsonStr -> JSONUtil.toBean((String) jsonStr, ShoppingCart.class))
                    .collect(Collectors.toList());
            if (!shoppingCart.isEmpty()) {
                return shoppingCart;
            }
        }
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        //查询未支付
        wrapper.eq(ShoppingCart::getUserId,userId).eq(ShoppingCart::getIsPaid,0);
        List<ShoppingCart> shoppingCartDB = shoppingCartMapper.selectList(wrapper);
        if (shoppingCartDB == null) {
            return Collections.emptyList();
        }
        Map<String,String> map = new HashMap<>();
        for(ShoppingCart s : shoppingCartDB){
            Integer productId = s.getProductId();
            Product product = productService.getById(productId);
            s.setProduct(product);
            map.put(String.valueOf(s.getProductId()),JSONUtil.toJsonStr(s));
        }
        stringRedisTemplate.opsForHash().putAll(key,map);
        return shoppingCartDB;
    }

    @Override
    public Boolean deleteShoppingCart(String userId, String productId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getProductId, productId);
        boolean success = this.remove(wrapper);
        if(success){
            //清除缓存
            String shoppingKey = SHOPPING_CART_KEY + ":" + userId;
            stringRedisTemplate.opsForHash().delete(shoppingKey,productId);
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean updateShoppingCart(String userId, String productId, Integer num) {
        //查询该商品可购买的最大值
        Integer maxPurchasableNum = productService.getMaxPurchasableNum(Integer.valueOf(productId));
        if (maxPurchasableNum < num) {
            return false;
        }
        //更改购物车中的数量
        Boolean flag = shoppingCartMapper.updateShoppingCart(userId, productId, num);
        if (Boolean.TRUE.equals(flag)) {
            //更新缓存
            String shoppingKey = SHOPPING_CART_KEY + ":" + userId;
            String json = (String) stringRedisTemplate.opsForHash().get(shoppingKey, productId);
            ShoppingCart shoppingCart = JSONUtil.toBean(json, ShoppingCart.class);
            shoppingCart.setNum(num);
            stringRedisTemplate.opsForHash().put(shoppingKey,productId,JSONUtil.toJsonStr(shoppingCart));
            return Boolean.TRUE;
        } else {
            throw new BusinessException(REQUEST_SERVICE_ERROR, UPDATE_FAIL);
        }
    }

    @Override
    public Map<ShoppingCart, Integer> addShoppingCart(String userId, String productId) {
        // 首次添加0 重复添加1 已达上限-1
        Map<ShoppingCart, Integer> map = new HashMap<>();
        //查询购物车中是否有该商品
        String shoppingKey = SHOPPING_CART_KEY + ":" + userId;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(shoppingKey);
        List<String> productIds = entries.keySet().stream().map(String.class::cast).collect(Collectors.toList());
        if (productIds.isEmpty()) {
            //首次添加
            return this.addShoppingCartFirst(userId, productId);
        } else {
            if (productIds.contains(productId)) {
                //重复添加
                ShoppingCart holding = JSONUtil.toBean((String) entries.get(productId), ShoppingCart.class);
                //如果商品已到限购数量
                Integer hadNum = holding.getNum();
                Integer maxPurchasableNum = productService.getMaxPurchasableNum(Integer.valueOf(productId));
                if (hadNum.equals(maxPurchasableNum)) {
                    //已达上限
                    map.put(holding, -1);
                    return map;
                }
                //更新购物车，重复添加
                this.updateShoppingCart(userId, productId, hadNum + 1);
                holding.setNum(hadNum + 1);
                map.put(holding, 1);
                return map;
            } else {
                //首次添加
                return this.addShoppingCartFirst(userId, productId);
            }
        }
    }

    public Map<ShoppingCart, Integer> addShoppingCartFirst(String userId, String productId) {
        // 首次添加0 重复添加1 已达上限-1
        Map<ShoppingCart, Integer> map = new HashMap<>();

        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUserId(Integer.parseInt(userId));
        newShoppingCart.setProductId(Integer.parseInt(productId));
        Product product = productService.getById(productId);
        if(product.getProductNum()<= product.getProductSales()){
            throw new BusinessException(REQUEST_SERVICE_ERROR,INVENTORY_SHORTAGE_ERROR);
        }
        newShoppingCart.setProduct(product);
        newShoppingCart.setNum(1);
        boolean save = this.save(newShoppingCart);
        if (save) {
            //首次添加
            //添加到缓存中
            String shoppingKey = SHOPPING_CART_KEY + ":" + userId;
            stringRedisTemplate.opsForHash().put(shoppingKey, productId, JSONUtil.toJsonStr(newShoppingCart));
            map.put(newShoppingCart, 0);
            return map;
        } else {
            //添加失败
            return Collections.emptyMap();
        }
    }

    @Override
    public Boolean updateProductStatus(String userId, String productId) {
        LambdaUpdateWrapper<ShoppingCart> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getProductId, productId)
                .set(ShoppingCart::getIsPaid, 1);
        boolean success = update(wrapper);
        if(success){
            //清除缓存
            String shoppingKey = SHOPPING_CART_KEY + ":" + userId;
            stringRedisTemplate.opsForHash().delete(shoppingKey,productId);
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
}




