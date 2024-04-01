package com.maxdeng.springbootmall.service;

import com.maxdeng.springbootmall.dto.CreateOrderRequest;
import com.maxdeng.springbootmall.dto.OrderQueryParams;
import com.maxdeng.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest, String username);

    Order getOrderById(Integer orderId);

    List<Order> getOrders(OrderQueryParams orderQueryParams, String username);

    Integer countOrders(OrderQueryParams orderQueryParams);
}
