package com.maxdeng.springbootmall.dao;

import com.maxdeng.springbootmall.dto.OrderQueryParams;
import com.maxdeng.springbootmall.model.Order;
import com.maxdeng.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Integer countOrders(OrderQueryParams orderQueryParams);
}
