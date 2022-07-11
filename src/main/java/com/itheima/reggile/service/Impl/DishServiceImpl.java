package com.itheima.reggile.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggile.dto.DishDto;
import com.itheima.reggile.entity.Dish;
import com.itheima.reggile.entity.DishFlavor;
import com.itheima.reggile.mapper.DishMapper;
import com.itheima.reggile.service.DishFlavorService;
import com.itheima.reggile.service.DishService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
       //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        //菜品id
        Long id = dishDto.getId();
        System.out.println(id);
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for(int i=0;i<flavors.size();i++)
        {
            flavors.get(i).setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish=this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(LambdaQueryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(LambdaQueryWrapper);
        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        for(int i=0;i<flavors.size();i++)
        {
            DishFlavor dishFlavor = flavors.get(i);
            dishFlavor.setDishId(dishDto.getId());
            dishFlavorService.save(dishFlavor);
        }
    }

    @Override
    public void adjustStatus(int status, Long[] ids) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //查询到需要修改状态的菜品
        queryWrapper.in(Dish::getId,ids);
        List<Dish> list = this.list(queryWrapper);

        //对菜品的中的status进行批量调整
        for(int i=0;i<list.size();i++)
        {
            Dish dish = list.get(i);
            dish.setStatus(status);
        }

        this.updateBatchById(list);
    }


}

