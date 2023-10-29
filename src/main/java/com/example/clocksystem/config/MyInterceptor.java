package com.example.clocksystem.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        HttpSession session = request.getSession();
        // 允许静态资源的访问  
        if (requestURI.contains("/static/")) {
            return true;
        }

        // 允许主页访问
        if ("/".equals(requestURI)) {
            return true;
        }

        // 允许POST请求
        if ("POST".equals(method)) {
            return true;
        }

        // 如果请求路径是登录页面，则直接通过
        if (requestURI.contains("/login") || requestURI.contains("/logout")) {
            return true;
        }

        //如果请求路径是异常页面,则直接通过
        if(requestURI.contains("404")){
            return true;
        }

        //不拦截找回密码
        if(requestURI.contains("findPassword")){
            return true;
        }

        // 不拦截主页
        if (requestURI.contains("index")){
            return true;
        }

        // 如果是错误页面拦截到404
        if(requestURI.contains("error")){
            return true;
        }

        //允许已经登录的用户使用他们的功能页面
        if(session.getAttribute("admin")!=null &&requestURI.contains("/admin/")){
            return true;
        }

        if(session.getAttribute("student")!=null &&requestURI.contains("/student/")){
            return true;
        }

        if(session.getAttribute("teacher")!=null &&requestURI.contains("/teacher/")){
            return true;
        }
        if(session.getAttribute("office")!=null &&requestURI.contains("/office/")){
            return true;
        }
        if(session.getAttribute("society")!=null &&requestURI.contains("/society/")){
            return true;
        }

        // 其他所有请求都重定向到登录页面  
        response.sendRedirect("/");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}