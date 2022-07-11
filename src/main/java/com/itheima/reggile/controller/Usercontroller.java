package com.itheima.reggile.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggile.common.R;
import com.itheima.reggile.entity.User;
import com.itheima.reggile.service.UserService;
import com.itheima.reggile.utils.SMSUtils;
import com.itheima.reggile.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class Usercontroller {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody  User user, HttpSession session)
    {
        //获取手机号
        String phone=user.getPhone();
        if(StringUtils.isNotEmpty(phone))
        {
            //生产验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            System.out.println("***********");
            System.out.println(code);
            System.out.println("***********");

            //调用阿里云的提供的API短信服务完成发信息服务
//            SMSUtils.sendMessage("外卖","",phone,code);
            session.setAttribute(phone,code);
            return  R.success("手机短信已发生");
        }
        return R.error("手机号码未输入");
    }

    @RequestMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session)
    {
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //如果比对成功，注册成功
        Object sessionCode = session.getAttribute(phone);
        //session中获取验证码
        if(sessionCode!=null&&code.equals(sessionCode))
        {
            //如果是新用户完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            //为空即为新的用户
            if(user==null)
            {
                user= new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);

            }
            session.setAttribute("user",user.getId());
            return  R.success(user);
        }
        return  R.error("登录失败");

    }

    @PostMapping("loginout")
    public R<String> loginOut(HttpServletRequest request)
    {
        //清理session域中保存的员工id信息
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


}
