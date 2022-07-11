package com.itheima.reggile.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggile.common.R;
import com.itheima.reggile.dto.DishDto;
import com.itheima.reggile.entity.Category;
import com.itheima.reggile.entity.Dish;
import com.itheima.reggile.entity.DishFlavor;
import com.itheima.reggile.service.CategoryService;
import com.itheima.reggile.service.DishFlavorService;
import com.itheima.reggile.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {


    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto)
    {
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);
       return  R.success("新增加菜品成功");
    }
    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name)
    {
        //构造分页构造器对象
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        lambdaQueryWrapper.like(name!=null,Dish::getName,name);
        //添加排序条件
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(dishPage, lambdaQueryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> cords=new ArrayList<>();
        //多表联查，对加强类中的CategoryName赋值
        for(int i=0;i<records.size();i++)
        {
            DishDto dishDto = new DishDto();
            Dish dish = records.get(i);
            Long id = dish.getCategoryId();
            BeanUtils.copyProperties(dish,dishDto);
            cords.add(dishDto);
            Category category = categoryService.getById(id);
            if(category!=null)
            {
                String name1 = category.getName();
                dishDto.setCategoryName(name);
            }
        }
        dishDtoPage.setRecords(cords);
        System.out.println(dishDtoPage.getRecords());

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    /**
     * 调整状态
     * @param status 前端已进行更新
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updata(@PathVariable("status") Integer status,Long[] ids)
    {
        System.out.println(")))))))))))))))))");
        System.out.println(status);
            dishService.adjustStatus(status,ids);
            return R.success("调整成功");

    }

    /**
     * 根据id进行删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long[] ids)
    {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        dishService.remove(queryWrapper);
        return R.success("删除成功");
    }


    /**
     * 根据条件查询对应的菜品数据
    * @param dish
    * @return
    */

    @GetMapping("list")
    public <list> R<List<DishDto>> list(Dish dish)
    {
        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(dish.getName()!=null,Dish::getName,dish.getName());
        //添加条件，查询状态为1（起售状态）的菜品
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        lambdaQueryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(lambdaQueryWrapper);
        List<DishDto> listInfo=new ArrayList<DishDto>();
        //对增强类进行的口味进行赋值展示点（用户功能的扩展）
        for(int i=0;i<list.size();i++)
        {
            DishDto dishDto = new DishDto();
            Dish dish1 = list.get(i);
            BeanUtils.copyProperties(dish1,dishDto);
            Long id = list.get(i).getId();
            //获取菜品id
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
            //到口味中查询出口味
            List<DishFlavor> DishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
           //对增强类进行赋值
            dishDto.setFlavors(DishFlavorList);
            listInfo.add(i,dishDto);
        }
        return R.success(listInfo);
    }





}
