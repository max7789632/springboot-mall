package com.maxdeng.springbootmall.dao;

import com.maxdeng.springbootmall.dto.UserRegisterRequest;
import com.maxdeng.springbootmall.model.Role;
import com.maxdeng.springbootmall.model.User;

import java.util.List;

public interface UserDao {

    User getUserById(Integer userId);

    User getUserByEmail(String email);

    Integer createUser(UserRegisterRequest userRegisterRequest);

    List<Role> getRolesByUserId(Integer userId);

    void createUserHasRole(Integer userId);
}
