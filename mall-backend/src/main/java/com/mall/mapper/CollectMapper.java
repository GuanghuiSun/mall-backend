package com.mall.mapper;

import com.mall.model.domain.Collect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.text.CollationElementIterator;
import java.util.List;

/**
* @author huawei
* @description 针对表【collect(收藏)】的数据库操作Mapper
* @createDate 2022-03-19 22:47:30
* @Entity com.mall.model.domain.Collect
*/
public interface CollectMapper extends BaseMapper<Collect> {
    /**
     * 获取用户所有收藏
     * @param userId 用户id
     * @return
     */
    List<Collect> getCollect(String userId);
}




