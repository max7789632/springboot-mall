package com.maxdeng.springbootmall.controller;

import com.maxdeng.springbootmall.dto.CreateOrderRequest;
import com.maxdeng.springbootmall.dto.OrderQueryParams;
import com.maxdeng.springbootmall.model.Order;
import com.maxdeng.springbootmall.service.OrderService;
import com.maxdeng.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Validated
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<Order> createOrder(
            Authentication authentication,
            @PathVariable Integer userId,
            @RequestBody @Valid CreateOrderRequest createOrderRequest
    ) {
        // 取得登入中使用者的帳號
        String username = authentication.getName();

        Integer orderId = orderService.createOrder(userId, createOrderRequest, username);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);

    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(
            Authentication authentication,
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset)
    {
        // 取得登入中使用者的帳號
        String username = authentication.getName();

        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        // 取得 order list
        List<Order> orderList = orderService.getOrders(orderQueryParams, username);

        // 取得 order 總數
        Integer total = orderService.countOrders(orderQueryParams);

        // 分頁
        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResult(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
