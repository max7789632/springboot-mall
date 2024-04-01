package com.maxdeng.springbootmall.controller;

import com.maxdeng.springbootmall.dto.UserRegisterRequest;
import com.maxdeng.springbootmall.model.User;
import com.maxdeng.springbootmall.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // 帳號註冊
    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        Integer userId = userService.register(userRegisterRequest);

        User user = userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // 帳號登入
//    @PostMapping("/users/login")
//    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
//        User user = userService.login(userLoginRequest);
//
//        return ResponseEntity.status(HttpStatus.OK).body(user);
//    }
}
