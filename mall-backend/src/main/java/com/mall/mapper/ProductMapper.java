package com.mall.mapper;

import com.mall.model.domain.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author sgh
*/
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}




