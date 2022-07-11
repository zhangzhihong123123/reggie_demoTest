package com.itheima.reggile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggile.dto.SetmealDto;
import com.itheima.reggile.entity.Setmeal;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 调整套餐状态
     * @param ids
     */
    public void  adjustStatus(int status,List<Long> ids);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);


    /**
     * 回显套餐数据：根据套餐id查询套餐
     * @return
     */
    SetmealDto getDate(Long id);



}
