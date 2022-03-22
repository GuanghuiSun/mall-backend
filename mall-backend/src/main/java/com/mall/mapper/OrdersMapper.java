package com.mall.mapper;

import com.mall.model.domain.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author huawei
* @description 针对表【orders】的数据库操作Mapper
* @createDate 2022-03-19 21:49:09
* @Entity com.mall.model.domain.Orders
*/
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 获取所有订单
     * @param userId 用户id
     * @return
     */
    List<Orders> getOrders(String userId);

}




