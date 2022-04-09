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
     *
     * @return
     */
    List<Carousel> getCarousel();

    /**
     * 添加轮播图
     *
     * @param carousel 轮播图
     * @return
     */
    Integer addCarousel(Carousel carousel);

    /**
     * 删除轮播图
     *
     * @param carouselId 轮播图id
     * @return
     */
    Boolean deleteById(Integer carouselId);
}
