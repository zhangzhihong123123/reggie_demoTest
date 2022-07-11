package com.itheima.reggile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggile.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
       boolean removeid(Long id);
}
