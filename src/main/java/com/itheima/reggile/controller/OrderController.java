package com.itheima.reggile.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggile.common.BaseContext;
import com.itheima.reggile.common.R;
import com.itheima.reggile.dto.OrdersDto;
import com.itheima.reggile.entity.Orders;
import com.itheima.reggile.entity.User;
import com.itheima.reggile.service.OrderService;
import com.itheima.reggile.service.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.tags.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders)
    {
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 员工查看订单信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public R<Page> page(int page, int pageSize, Long number, String beginTime,String endTime)
    {
        Page ordersPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
       //按照时间先进行排序
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //订单号查询
        queryWrapper.eq(number!=null,Orders::getNumber,number);
        //按照时间查询
        queryWrapper.le(endTime!=null,Orders::getOrderTime,endTime);
        queryWrapper.ge(endTime!=null,Orders::getOrderTime,beginTime);
        orderService.page(ordersPage,queryWrapper);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        BeanUtils.copyProperties(ordersPage,ordersDtoPage,"records");
        List<Orders> ordersListr=ordersPage.getRecords();
        List<OrdersDto> ordersDtoListr=new ArrayList<OrdersDto>();
        //为增强类中用户名赋值
        for(int i=0;i<ordersListr.size();i++)
        {
            OrdersDto ordersDto = new OrdersDto();
            Orders orders = ordersListr.get(i);
            BeanUtils.copyProperties(orders,ordersDto);
            //获取订单中的用户id
            Long userId = orders.getUserId();
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //根据用户id到用户表中查询用户名
            lambdaQueryWrapper.eq(User::getId,userId);
            User one = userService.getOne(lambdaQueryWrapper);
            //如果用户没有设置用户名，默认以电话号为名
            if(one.getName()!=null)
            {
                System.out.println("空————————————");
                ordersDto.setUserName(one.getName());
            }
            else {
                //赋值
                ordersDto.setUserName(one.getPhone());
            }
            //插入
            ordersDtoListr.add(i,ordersDto);
        }
        ordersDtoPage.setRecords(ordersDtoListr);

        System.out.println("******************************");
        System.out.println(ordersDtoListr);
        System.out.println("******************************");

        return R.success(ordersDtoPage);
    }

    /**
     * 对订单状态进修改，前端已封装好订单详情（状态+1）
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> up(@RequestBody Orders orders)
    {

      orderService.updateById(orders);
        return R.success("修改成功");
    }


    /**
     * 用户下单查看自己下单详情
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(int page,int pageSize)
    {
        Page<Orders> ordersPage = new Page<>();
        LambdaQueryWrapper<Orders> ordersQueryWrapper = new LambdaQueryWrapper<>();
        Long currentId = BaseContext.getCurrentId();
        //按照时间先进行排序
        ordersQueryWrapper.orderByDesc(Orders::getOrderTime);
        //再订单表中进行查询操作
        ordersQueryWrapper.eq(Orders::getUserId,currentId);

        orderService.page(ordersPage,ordersQueryWrapper);
       return R.success(ordersPage);


    }

    /**
     * 再来一单跳转
     * @return
     */
    @PostMapping("/again")
    public R<String> again()
    {

        return R.success("success");
    }

}
