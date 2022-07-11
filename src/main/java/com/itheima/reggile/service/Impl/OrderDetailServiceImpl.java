package com.itheima.reggile.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggile.entity.OrderDetail;
import com.itheima.reggile.mapper.OrderDetailMapper;
import com.itheima.reggile.service.OrderDetailService;

import org.springframework.stereotype.Service;


@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper,OrderDetail> implements OrderDetailService {
}
