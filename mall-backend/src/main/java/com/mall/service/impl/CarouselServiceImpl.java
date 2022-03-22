package com.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.model.domain.Carousel;
import com.mall.service.CarouselService;
import com.mall.mapper.CarouselMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author sgh
*/
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel>
    implements CarouselService{

    @Resource
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> getCarousel() {
        return this.list();
    }
}




