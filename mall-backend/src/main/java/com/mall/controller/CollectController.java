package com.mall.controller;

import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.constant.MessageConstant;
import com.mall.model.domain.Collect;
import com.mall.model.request.CollectRequest;
import com.mall.service.CollectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    /**
     * 获取收藏列表
     * @param userId 用户id
     * @return
     */
    @GetMapping("/getCollect")
    public BaseResponse<List<Collect>> getCollect(String userId){
        if(StringUtils.isAnyBlank(userId)){
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        List<Collect> collect = collectService.getCollect(userId);
        if(collect==null){
            return ResultUtils.error(null,MessageConstant.SELECT_FAIL);
        }else{
            return ResultUtils.success(collect,MessageConstant.SELECT_SUCCESS);
        }
    }

    /**
     * 删除收藏列表指定商品
     * @param collectRequest 删除请求体
     * @return
     */
    @PostMapping("/deleteCollect")
    public BaseResponse<Boolean> deleteCollect(@RequestBody CollectRequest collectRequest){
        if(collectRequest==null){
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        String userId = collectRequest.getUserId();
        String productId = collectRequest.getProductId();
        if(StringUtils.isAnyBlank(userId,productId)){
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        Boolean result = collectService.deletedCollect(userId, productId);
        if(Boolean.TRUE.equals(result)){
            return ResultUtils.success(true,MessageConstant.DELETE_SUCCESS);
        }else{
            return ResultUtils.error(false,MessageConstant.DELETE_FAIL);
        }
    }

    /**
     * 添加商品到收藏列表
     * @param collectRequest 添加请求体
     * @return
     */
    @PostMapping("/addCollect")
    public BaseResponse<Boolean> addCollect(@RequestBody CollectRequest collectRequest){
        if(collectRequest==null){
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        String userId = collectRequest.getUserId();
        String productId = collectRequest.getProductId();
        if(StringUtils.isAnyBlank(userId,productId)){
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        Integer result = collectService.addCollect(userId, productId);
        if(result == 0){
            return ResultUtils.error(false,MessageConstant.ADD_REPEAT_FAIL);
        }else if(result == 1){
            return ResultUtils.success(true,MessageConstant.ADD_SUCCESS);
        }else{
            return ResultUtils.error(false,MessageConstant.ADD_FAIL);
        }
    }

}
