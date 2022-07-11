package com.itheima.reggile.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggile.common.R;
import com.itheima.reggile.entity.Category;

import com.itheima.reggile.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

  @Autowired
  private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category:{}", category);
      categoryService.save(category);
        return R.success("新增分类成功");
    }


//  GEThttp://localhost:8080/category/page?page=1&pageSize=10
    @GetMapping("/page")
  public R<Page> page(@RequestParam Integer page,@RequestParam Integer pageSize)
    {
      Page<Category> PageInfo = new Page<Category>(page,pageSize);
      LambdaQueryWrapper<Category> QueryWrapper = new LambdaQueryWrapper<>();
      QueryWrapper.orderByAsc(Category::getSort);
      categoryService.page(PageInfo,QueryWrapper);
      return R.success(PageInfo);
    }

  /**
   * 根据id删除
   * @param id
   * @return
   */
    @DeleteMapping
  public R<String> delete(@RequestParam("ids") Long id){
      System.out.println(id);
        System.out.println("********************");
        System.out.println("********************");

        System.out.println("********************");

     categoryService.remove(id);
     return R.success("删除成功");
    }


    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);

        categoryService.updateById(category);

        return R.success("修改分类信息成功");
    }


    /**
     * 查询所有的菜品种类用来前端展示
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category)
    {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println(category);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&");
        //条件构造器
        LambdaQueryWrapper<Category> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        LambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        LambdaQueryWrapper.orderByAsc(Category::getCreateTime);
        List<Category> list = categoryService.list(LambdaQueryWrapper);
        return  R.success(list);
    }


}