package com.itheima.reggile.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggile.entity.ShoppingCart;
import com.itheima.reggile.mapper.ShoppingCartMapper;
import com.itheima.reggile.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImp extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements
        ShoppingCartService {
}
