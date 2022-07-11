package com.itheima.reggile.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggile.entity.User;
import com.itheima.reggile.mapper.UserMapper;

import com.itheima.reggile.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {
}
