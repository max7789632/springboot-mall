package com.maxdeng.springbootmall.service.impl;

import com.maxdeng.springbootmall.dao.UserDao;
import com.maxdeng.springbootmall.dto.UserRegisterRequest;
import com.maxdeng.springbootmall.model.User;
import com.maxdeng.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 檢查註冊的 email 是否為空值
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (user != null) {
            log.warn("該 email {} 已被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 BCrypt 生成密碼的雜湊值
        String hashedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());
        userRegisterRequest.setPassword(hashedPassword);

        // 創建帳號
        Integer userId = userDao.createUser(userRegisterRequest);
        // 給予一般會員權限
        userDao.createUserHasRole(userId);
        return userId;
    }
}
