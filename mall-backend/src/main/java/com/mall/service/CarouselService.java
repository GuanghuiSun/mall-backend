package com.mall.service;

import com.mall.model.domain.Carousel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author sgh
*/
public interface CarouselService extends IService<Carousel> {

    /**
     * 获取轮播图
     * @return
     */
    List<Carousel> getCarousel();
}
