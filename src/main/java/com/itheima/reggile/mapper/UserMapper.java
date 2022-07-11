package com.itheima.reggile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggile.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
