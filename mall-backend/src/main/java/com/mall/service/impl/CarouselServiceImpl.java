package com.mall.service.impl;


import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.model.domain.Carousel;
import com.mall.service.CarouselService;
import com.mall.mapper.CarouselMapper;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mall.constant.MessageConstant.*;
import static com.mall.constant.RedisConstant.CACHE_CAROUSEL_KEY;

/**
 * @author sgh
 */
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel>
        implements CarouselService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<Carousel> getCarousel() {
        //先查缓存
        Set<String> range = stringRedisTemplate.opsForZSet().reverseRange(CACHE_CAROUSEL_KEY, 0, -1);
        if (range != null && !range.isEmpty()) {
            return range.stream()
                    .map(jsonStr -> JSONUtil.toBean(jsonStr, Carousel.class))
                    .collect(Collectors.toList());
        }
        //未命中缓存
        List<Carousel> list = this.list();
        //写入缓存
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        for (Carousel carousel : list) {
            typedTuples.add(new DefaultTypedTuple<>
                    (JSONUtil.toJsonStr(carousel),
                            Convert.convert(Double.class, carousel.getCarouselId())));
        }
        stringRedisTemplate.opsForZSet().add(CACHE_CAROUSEL_KEY, typedTuples);
        return list;
    }

    @Override
    public BaseResponse<Integer> addCarousel(Carousel carousel) {
        boolean success = this.save(carousel);
        if (!success) {
            return ResultUtils.fail(ADD_SUCCESS);
        }
        carousel.setIsDeleted((byte) 0);
        // 更新缓存
        stringRedisTemplate.opsForZSet().add(CACHE_CAROUSEL_KEY, JSONUtil.toJsonStr(carousel), carousel.getCarouselId());

        return ResultUtils.success(carousel.getCarouselId(), ADD_SUCCESS);
    }

    @Override
    public BaseResponse<Boolean> deleteById(Integer carouselId) {
        Carousel carousel = getById(carouselId);
        if (carousel == null) {
            return ResultUtils.error(false, CAROUSEL_NOT_EXIST);
        }
        boolean success = removeById(carouselId);
        if (success) {
            //更新缓存
            stringRedisTemplate.opsForZSet().remove(CACHE_CAROUSEL_KEY, JSONUtil.toJsonStr(carousel));
            return ResultUtils.success(true, DELETE_SUCCESS);
        }
        return ResultUtils.error(false, DELETE_FAIL);
    }
}




