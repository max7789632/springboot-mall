package com.maxdeng.springbootmall.service;

import com.maxdeng.springbootmall.dto.UserRegisterRequest;
import com.maxdeng.springbootmall.model.User;

public interface UserService {

    User getUserById(Integer userId);

    Integer register(UserRegisterRequest userRegisterRequest);
}
