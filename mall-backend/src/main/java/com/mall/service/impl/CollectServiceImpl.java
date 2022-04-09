package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.exception.BusinessException;
import com.mall.model.domain.Collect;
import com.mall.service.CollectService;
import com.mall.mapper.CollectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.mall.base.ErrorCode.REQUEST_SERVICE_ERROR;
import static com.mall.constant.MessageConstant.ADD_REPEAT_FAIL;

/**
 * @author sgh
 * @createDate 2022-03-19 22:47:30
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect>
        implements CollectService {

    @Resource
    private CollectMapper collectMapper;

    @Override
    public List<Collect> getCollect(String userId) {
        List<Collect> collect = collectMapper.getCollect(userId);
        if(collect == null){
            return Collections.emptyList();
        }else{
            return collect;
        }
    }

    @Override
    public Boolean deletedCollect(String userId, String productId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId).eq(Collect::getProductId, productId);
        return this.remove(wrapper);
    }

    @Override
    public Boolean addCollect(String userId, String productId) {
        //先判断收藏夹中是否存在这组关系
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId).eq(Collect::getProductId, productId);
        Collect result = this.getOne(wrapper);
        if (result != null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR,ADD_REPEAT_FAIL);
        }
        Collect collect = new Collect();
        collect.setUserId(Integer.parseInt(userId));
        collect.setProductId(Integer.parseInt(productId));
        return this.save(collect);
    }
}




