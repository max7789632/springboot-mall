package com.maxdeng.springbootmall.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

                // 設定 Session 的創建機制
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )

                // 關閉 CSRF 保護(方便開發)
//                .csrf(csrf -> csrf.disable())

                // 設定 CSRF 保護
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(createCsrfHandler())
                        // 禁用 CSRF 的路徑，因為註冊帳號不需要登入
                        .ignoringRequestMatchers("/users/register")
                )

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

                // 增加 cors 設定
                .cors(cors -> cors
                        .configurationSource(createCorsConfig())
                )

                // 插入 MyFilter1 到 BasicAuthenticationFilter 之前
                .addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class)

                .build();
    }

    // 處理 Cors 不同源問題
    private CorsConfigurationSource createCorsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        // 允許的請求來源 Ex: "http://examp.com"
        config.setAllowedOrigins(List.of("*"));

        // 允許的 request header
        config.setAllowedHeaders(List.of("*"));

        // 允許的 http method Ex: "GET", "POST"
        config.setAllowedMethods(List.of("*"));

        // 允許前端帶上 cookie，需指定 setAllowedOrigins 來源
//        config.setAllowCredentials(true);
        // 設定 Preflight 請求結果，可以被瀏覽器 cache 幾秒
        config.setMaxAge(3600L);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // 設定 CSRF 保護
    private CsrfTokenRequestHandler createCsrfHandler() {
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName(null);

        return csrfHandler;
    }
}
