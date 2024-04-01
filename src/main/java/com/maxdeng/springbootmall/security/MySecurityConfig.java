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
                        // 新增、修改、刪除商品
                        .requestMatchers(HttpMethod.POST, "/products", "/users/{userId}/orders").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/{productId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/{productId}").hasRole("ADMIN")

                        // ***一般會員可以使用的API***
                        .requestMatchers(HttpMethod.GET, "/users/{userId}/orders").hasAnyRole("ADMIN", "NORMAL_USER")
                        .requestMatchers(HttpMethod.POST, "/users/{userId}/orders").hasAnyRole("ADMIN", "NORMAL_USER")

                        // ***不需登入也能使用的API***
                        // 註冊會員
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        // 查詢商品
                        .requestMatchers(HttpMethod.GET, "/products", "/products/**").permitAll()

                        // ***其餘沒設定的API都不能訪問***
                        .anyRequest().denyAll()
                )
                .build();
    }
}
