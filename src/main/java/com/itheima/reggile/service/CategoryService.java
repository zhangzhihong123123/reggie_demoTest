package com.itheima.reggile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggile.entity.Category;


public interface CategoryService extends IService<Category> {
        public void  remove(Long id);
}
