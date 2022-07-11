package com.itheima.reggile.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggile.common.R;
import com.itheima.reggile.dto.DishDto;
import com.itheima.reggile.dto.SetmealDto;
import com.itheima.reggile.entity.*;

import com.itheima.reggile.service.CategoryService;
import com.itheima.reggile.service.DishService;
import com.itheima.reggile.service.SetmealDishService;
import com.itheima.reggile.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
//RPOST http://localhost:8080/setmeal
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;




    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
//        log.info("套餐信息：{}",setmealDto);

        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

//


    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name)
    {
        //分页构造器对象
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        //添加查询条件，根据name进行like模糊查询
        LambdaQueryWrapper<Setmeal> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        LambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage,LambdaQueryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(setmealPage,dtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> cords=new ArrayList<>();
        //拿出基础类的每一个数据为增强类的每一个赋值
        for(int i=0;i<records.size();i++)
        {
            Setmeal setmeal = records.get(i);
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            //多表查询，先通过该套餐中的CategoryId查到Categor表的对应的id的对象category
            //再根据id查询到该套餐的名字为增强类赋值
            Category category = categoryService.getById(setmeal.getCategoryId());
            if(category!=null)
            {
                String name1 = category.getName();
                setmealDto.setCategoryName(name1);
            }
            cords.add(i,setmealDto);
        }
        dtoPage.setRecords(cords);
        return R.success(dtoPage);

    }

//    RPOS Thttp://localhost:8080/setmeal/status/0?ids=1528533927822716930

    /**
     * 根据id对停售功能进行调整
     * @param ids
     * @return
     */
     @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status,@RequestParam("ids") List<Long> ids)
     {
         System.out.println("&&&&&&&&&&&&&&&&&&***");
         setmealService.adjustStatus(status,ids);
         return R.success("修改成功");
     }


     @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> ids)
     {
         setmealService.removeWithDish(ids);
         return R.success("删除成功");
     }

//    http://localhost:8080/setmeal/list?categoryId=1413342269393674242&status=1

    @GetMapping("list")
    public R<List<Setmeal>> list(Long categoryId)
    {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,categoryId);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
       return  R.success(list);
    }



    //对增强类的中套餐具体的信息进行赋值
//       for (int i=0;i<list.size();i++)
//    {
//        SetmealDto setmealDto = new SetmealDto();
//        Setmeal setmeal = list.get(i);
//        BeanUtils.copyProperties(setmeal,setmealDto);
//        //获取套餐中id
//        Long id = setmeal.getId();
//        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        //查询SetmealDish表，查出该套餐含有的菜品
//        dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
//        List<SetmealDish> dish = setmealDishService.list(dishLambdaQueryWrapper);
//        //为套餐增强类中菜品数组赋值
//        setmealDto.setSetmealDishes(dish);
//        listInfo.add(i,setmealDto);
//    }


    /**
     * 获取具体套餐中具体菜品
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List<Dish>> dish(@PathVariable("id") Long id)
    {

        //根据套餐id去查询SetmealDish表，查出该套餐含有的菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

      List<Dish> dishList=new ArrayList<>();
        //根据SetmealDish套餐菜品id去Dish表中查询,每一个菜品的具体信息（获取菜品图片地址...）
      for(int i=0;i<list.size();i++)
      {
          SetmealDish setmealDish = list.get(i);
          Long dishId = setmealDish.getDishId();
          LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
          wrapper.eq(Dish::getId,dishId);
          Dish dish = dishService.getOne(wrapper);
          dishList.add(i,dish);
      }
      return R.success(dishList);

    }


    /**
     * 回显套餐数据：根据套餐id查询套餐
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getData(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getDate(id);

        return R.success(setmealDto);
    }

}
