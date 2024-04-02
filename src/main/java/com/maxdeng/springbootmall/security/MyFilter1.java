package com.maxdeng.springbootmall.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class MyFilter1 extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        System.out.println("自定義 Filter1");

        // extends OncePerRequestFilter 該 Filter只會被執行一次

        // 把 request 和 response 傳給下一個 Filter
        filterChain.doFilter(request, response);
    }
}
