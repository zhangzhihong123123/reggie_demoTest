package com.itheima.reggile.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggile.common.CustomException;
import com.itheima.reggile.dto.SetmealDto;
import com.itheima.reggile.entity.Setmeal;
import com.itheima.reggile.entity.SetmealDish;
import com.itheima.reggile.mapper.SetmeaMapper;
import com.itheima.reggile.service.SetmealDishService;
import com.itheima.reggile.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmeaMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存基本信息
        this.save(setmealDto);
        //保存到SetmealDishes表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long id = setmealDto.getId();
        for(int i=0;i<setmealDishes.size();i++)
        {
            SetmealDish setmealDish = setmealDishes.get(i);
            setmealDish.setSetmealId(id);
            setmealDishService.save(setmealDish);
        }
    }


    /**
     * 调整套餐状态
     * @param ids
     */
    @Override
    public void adjustStatus(int status,List<Long> ids) {

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据id查询到需要调整的对象
        lambdaQueryWrapper.in(Setmeal::getId,ids);
        List<Setmeal> list = this.list(lambdaQueryWrapper);
         //对状态进行调整
        for(int i=0;i<list.size();i++)
        {
            Setmeal setmeal = list.get(i);
            setmeal.setStatus(status);
        }
        this.updateBatchById(list);
    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        //查询套餐状态，确定是否可用删除
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if(count>0)
        {
            throw new CustomException("存在套餐在售卖中");
        }
        //如果可以删除，先删除套餐表中的数据---setmeal
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据----setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);


    }

    @Override
    public SetmealDto getDate(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        //在关联表中查询，setmealdish
        queryWrapper.eq(id!=null,SetmealDish::getSetmealId,id);

        if (setmeal != null){
            BeanUtils.copyProperties(setmeal,setmealDto);
            List<SetmealDish> list = setmealDishService.list(queryWrapper);
            setmealDto.setSetmealDishes(list);
            return setmealDto;
        }
        return null;
    }


}
