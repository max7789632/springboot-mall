package com.maxdeng.springbootmall.security;

import com.maxdeng.springbootmall.dao.UserDao;
import com.maxdeng.springbootmall.model.Role;
import com.maxdeng.springbootmall.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 從資料庫中查詢會員資料
        User user = userDao.getUserByEmail(username);

        String userEmail = user.getEmail();
        String userPassword = user.getPassword();

        // 權限部分
        List<Role> roleList = userDao.getRolesByUserId(user.getUserId());

        List<GrantedAuthority> authorities = convertToAuthorities(roleList);

        log.info("登入帳號: " + username + ", 帳號權限: " + authorities);

        // 轉換成 Spring Security 指定的 User 格式
        return new org.springframework.security.core.userdetails.User(userEmail, userPassword, authorities);
    }

    private List<GrantedAuthority> convertToAuthorities(List<Role> roleList) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roleList) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }

        return authorities;
    }
}
