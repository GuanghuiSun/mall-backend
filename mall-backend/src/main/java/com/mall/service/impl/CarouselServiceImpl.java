package com.mall.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.model.domain.Carousel;
import com.mall.service.CarouselService;
import com.mall.mapper.CarouselMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.mall.constant.RedisConstant.CACHE_CAROUSEL_KEY;

/**
* @author sgh
*/
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel>
    implements CarouselService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<Carousel> getCarousel() {
        List<String> range = stringRedisTemplate.opsForList().range(CACHE_CAROUSEL_KEY, 0, -1);
        if(range != null && !range.isEmpty()) {
            return range.stream()
                    .map(jsonStr-> JSONUtil.toBean(jsonStr,Carousel.class))
                    .collect(Collectors.toList());
        }
        //未命中缓存
        List<Carousel> list = this.list();
        //写入缓存
        List<String> collect = list.stream().map(JSONUtil::toJsonStr).collect(Collectors.toList());
        stringRedisTemplate.opsForList().rightPushAll(CACHE_CAROUSEL_KEY,collect);
        return list;
    }
}




