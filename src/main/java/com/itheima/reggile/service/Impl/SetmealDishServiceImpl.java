package com.itheima.reggile.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggile.dto.SetmealDto;
import com.itheima.reggile.entity.Setmeal;
import com.itheima.reggile.entity.SetmealDish;
import com.itheima.reggile.mapper.SetmealDishMapper;
import com.itheima.reggile.service.SetmealDishService;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper,
        SetmealDish>
implements SetmealDishService {


}
