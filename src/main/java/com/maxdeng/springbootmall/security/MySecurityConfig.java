package com.maxdeng.springbootmall.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())

                .authorizeHttpRequests(request -> request
                        // ***管理員可以使用的API***
                        // 商品新增
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        // 商品修改
                        .requestMatchers(HttpMethod.PUT, "/products/{productId}").hasRole("ADMIN")
                        // 商品刪除
                        .requestMatchers(HttpMethod.DELETE, "/products/{productId}").hasRole("ADMIN")

                        // ***一般會員可以使用的API***
                        // 訂單查詢
                        .requestMatchers(HttpMethod.GET, "/users/{userId}/orders").hasAnyRole("ADMIN", "NORMAL_USER")
                        // 訂單新增
                        .requestMatchers(HttpMethod.POST, "/users/{userId}/orders").hasAnyRole("ADMIN", "NORMAL_USER")

                        // ***不需登入也能使用的API***
                        // 帳號註冊
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        // 商品查詢、商品查詢+商品編號
                        .requestMatchers(HttpMethod.GET, "/products", "/products/{productId}").permitAll()

                        // ***其餘沒設定的API都不能訪問***
                        .anyRequest().denyAll()
                )
                .build();
    }
}
