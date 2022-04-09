package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.exception.BusinessException;
import com.mall.model.domain.ProductPicture;
import com.mall.service.ProductPictureService;
import com.mall.mapper.ProductPictureMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.mall.base.ErrorCode.GET_MESSAGE_ERROR;
import static com.mall.constant.MessageConstant.SELECT_FAIL;

/**
* @author sgh
* @description 针对表【product_picture】的数据库操作Service实现
* @createDate 2022-03-21 18:00:00
*/
@Service
public class ProductPictureServiceImpl extends ServiceImpl<ProductPictureMapper, ProductPicture>
    implements ProductPictureService{

    @Resource
    private ProductPictureMapper productPictureMapper;

    @Override
    public List<ProductPicture> getDetailsPicture(String productId) {
        LambdaQueryWrapper<ProductPicture> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductPicture::getProductId,productId);
        List<ProductPicture> result = productPictureMapper.selectList(wrapper);
        if(result == null || result.isEmpty()){
            throw new BusinessException(GET_MESSAGE_ERROR,SELECT_FAIL);
        }
        return result;
    }

}




