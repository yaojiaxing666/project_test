package com.test.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@Component
public class LoginFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        HttpSession session=request.getSession();

        String path=request.getRequestURI();

        String token = request.getHeader("TOKEN");

        /*if(path.indexOf("/login.jsp")>-1){//登录页面不过滤
            filterChain.doFilter(servletRequest, servletResponse);//递交给下一个过滤器
            return;
        }*/

        if(path.contains("/login/") && token==null){//未登录
            response.sendRedirect("login.jsp");

        }else{
            filterChain.doFilter(request, response);//放行，递交给下一个过滤器
        }
    }

    @Override
    public void destroy() {

    }
}
