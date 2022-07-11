package com.itheima.reggile.service.Impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggile.entity.Employee;
import com.itheima.reggile.mapper.EmployeeMapper;

import com.itheima.reggile.service.EmployeeService;
import org.springframework.stereotype.Service;


@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {

}

