package com.example.clocksystem.config;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class FileEncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法，可以添加初始化逻辑
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 设置请求编码，根据实际情况选择合适的编码方式
        String encoding = "UTF-8"; // 例如使用UTF-8编码
        httpRequest.setCharacterEncoding(encoding);
        httpResponse.setCharacterEncoding(encoding);

        // 继续执行后续过滤器和请求处理逻辑
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 销毁方法，可以添加资源清理逻辑
    }
}
