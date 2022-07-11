package com.itheima.reggile.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggile.common.BaseContext;
import com.itheima.reggile.common.R;
import com.itheima.reggile.entity.Employee;
import com.itheima.reggile.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@Service
@RequestMapping("/employee")
public class EmployController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping(value = "/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee)
    {

     //   1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3、如果没有查询到则返回登录失败结果
        if(emp==null)
        {
            return R.error("登录失败");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }
        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }
        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }


    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request)
    {
        //清理session域中保存的员工id信息
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee)
    {
        System.out.println(employee);
        //判断是不是管理员，如果不是直接返回失败，提示不可操作
        if(BaseContext.getCurrentId()!=1)
        {
            return R. error("权限不足，无法操作，请联系管理员");
        }

        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        //以下字段已设置自动填充
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        //获得当前登录用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
//        LambdaQueryWrapper<Employee> QueryWrapper = new LambdaQueryWrapper<>();
//        QueryWrapper.eq(Employee::getUsername,employee.getUsername());
//        Employee one = employeeService.getOne(QueryWrapper);
//        if(one!=null)
//        {
//            return R.error("用户名重复");
//        }
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name)
    {
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> QueryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        QueryWrapper.like(name!=null,Employee::getName,name);
        //添加排序条件
        QueryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,QueryWrapper);
        return  R.success(pageInfo);

    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
         //判断是不是管理员，如果不是直接返回失败，提示不可操作
        if(BaseContext.getCurrentId()!=1)
        {
            return R.error("权限不足，无法操作，请联系管理员");
        }
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById( @PathVariable Long id)
    {
        Employee employee = employeeService.getById(id);
        if(employeeService!=null)
        {
            return R.success(employee);
        }
        return R.error("没有查到该员工信息");
    }



}
