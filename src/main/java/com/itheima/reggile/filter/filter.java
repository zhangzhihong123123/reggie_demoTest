package com.itheima.reggile.filter;


import com.alibaba.fastjson.JSON;
import com.itheima.reggile.common.BaseContext;
import com.itheima.reggile.common.R;
import com.itheima.reggile.entity.Employee;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(filterName = "loginCheck",urlPatterns = "/*")
@Slf4j
public class filter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest)request;
        HttpServletResponse httpServletResponse=(HttpServletResponse) response;
//        log.info("拦截请求:{}",httpServletRequest.getRequestURI());
        //1、获取本次请求的URI
        String requestURI = httpServletRequest.getRequestURI();
        //定义不需要处理的请求路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/sendMsg"
        };
        //2、判断本次请求是否需要处理
        boolean check=check(urls,requestURI);
        //3、如果不需要处理，则直接放行
        if(check)
        {
            chain.doFilter(request,response);
            return;
        }
        //4、判断登录状态，如果已登录，则直接放行
        if(httpServletRequest.getSession().getAttribute("employee")!=null)
        {
            log.info("用户已登录，用户id为：{}",httpServletRequest.getSession().getAttribute("employee"));
            Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            chain.doFilter(request,response);
            return;
        }

        //4-2、判断登录状态，如果已登录，则直接放行
        if(httpServletRequest.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",httpServletRequest.getSession().getAttribute("user"));
            Long userId = (Long) httpServletRequest.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            chain.doFilter(httpServletRequest,response);
            return;
        }
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }



    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURL
     * @return
     */
    public boolean check(String[] urls,String requestURL){
        for(String url:urls)
        {
            boolean match=PATH_MATCHER.match(url,requestURL);
            if(match)
            {
                return  true;
            }
        }
        return false;
    }





}
