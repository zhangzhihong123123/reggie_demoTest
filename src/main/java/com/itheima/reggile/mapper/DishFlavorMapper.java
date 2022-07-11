package com.itheima.reggile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggile.entity.Dish;
import com.itheima.reggile.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}

