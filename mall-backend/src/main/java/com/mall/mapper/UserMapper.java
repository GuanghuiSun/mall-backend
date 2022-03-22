package com.mall.mapper;

import com.mall.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author huawei
* @description 针对表【user】的数据库操作Mapper
* @createDate 2022-03-20 10:48:34
* @Entity com.mall.model.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




