package com.mall.controller;

import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.constant.MessageConstant;
import com.mall.model.domain.Carousel;
import com.mall.service.CarouselService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.mall.constant.MessageConstant.*;

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

    /**
     * 添加
     * @param carousel 轮播图
     * @return
     */
    @PostMapping("/carousel")
    public BaseResponse<Integer> addCarousel(@RequestBody Carousel carousel) {
        if(carousel == null){
            return ResultUtils.error(null, REQUEST_FAIL);
        }
        return carouselService.addCarousel(carousel);
    }

    /**
     * 删除
     * @param carouselId 轮播图id
     * @return
     */
    @DeleteMapping("/carousel/{carouselId}")
    public BaseResponse<Boolean> deleteCarousel(@PathVariable("carouselId")Integer carouselId) {
        if(carouselId == null){
            return ResultUtils.error(null, REQUEST_FAIL);
        }
        return carouselService.deleteById(carouselId);
    }

}
