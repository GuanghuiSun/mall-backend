package com.mall.controller;

import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.constant.MessageConstant;
import com.mall.model.domain.Carousel;
import com.mall.service.CarouselService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourcesController {

    @Resource
    private CarouselService carouselService;

    /**
     * 获取轮播图
     * @return
     */
    @GetMapping("/carousel")
    public BaseResponse<List<Carousel>> getCarousels(){
        List<Carousel> carousel = carouselService.getCarousel();
        if(carousel == null){
            return ResultUtils.error(null, MessageConstant.SELECT_FAIL);
        }
        return ResultUtils.success(carousel,MessageConstant.SELECT_SUCCESS);
    }

}
