package com.mall.job;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.mall.model.domain.Product;
import com.mall.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mall.constant.RedisConstant.ACCESSORY_CATEGORY_KEY;
import static com.mall.constant.RedisConstant.APPLIANCE_CATEGORY_KEY;

/**
 * 定时刷新热门商品的redis
 *
 * @author sgh
 */
@Component
@Slf4j
public class HotProductJob {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ProductService productService;

    @Scheduled(cron = " 0 0 0 * * ?")
    private void refreshRedisTask() {
        log.info("refresh hot product in redis start...");
        //更新redis
        refreshRedis(APPLIANCE_CATEGORY_KEY);
        refreshRedis(ACCESSORY_CATEGORY_KEY);
        log.info("refresh hot product in redis end...");
    }

    /**
     * 更新redis
     * @param key 包含分类号的键名
     * @return
     */
    private void refreshRedis(String key) {
        //获取分类号
        String[] split = key.split("&");
        Integer[] categoryList = new Integer[split.length - 1];
        for(int i = 1; i < split.length; i++) {
            categoryList[i-1] = Integer.parseInt(split[i]);
        }
        ///获取新的列表
        List<Product> newList = productService.getProductByCategoryIds(categoryList);
        //写入redis
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        newList.forEach(product ->
                        typedTuples.add(new DefaultTypedTuple<>(JSONUtil.toJsonStr(product),
                                Convert.convert(Double.class, product.getProductSales()))));
        stringRedisTemplate.opsForZSet().add(key, typedTuples);
    }

}
