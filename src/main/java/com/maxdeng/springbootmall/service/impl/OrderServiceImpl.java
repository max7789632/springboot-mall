package com.maxdeng.springbootmall.service.impl;

import com.maxdeng.springbootmall.dao.OrderDao;
import com.maxdeng.springbootmall.dao.ProductDao;
import com.maxdeng.springbootmall.dao.UserDao;
import com.maxdeng.springbootmall.dto.BuyItem;
import com.maxdeng.springbootmall.dto.CreateOrderRequest;
import com.maxdeng.springbootmall.dto.OrderQueryParams;
import com.maxdeng.springbootmall.model.Order;
import com.maxdeng.springbootmall.model.OrderItem;
import com.maxdeng.springbootmall.model.Product;
import com.maxdeng.springbootmall.model.User;
import com.maxdeng.springbootmall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Transactional // 因為要同時在兩個 table 新增數據
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest, String username) {
        // 檢查 user 是否存在
        User user = userDao.getUserById(userId);

        if (user == null) {
            log.warn("查無使用者 UserId: {}", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!user.getEmail().equals(username)) {
            log.warn("不能創建其他使用者 {} 的訂單，登入中的使用者: {}", user.getEmail(), username);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            // 檢查 product 是否存在
            if (product == null) {
                log.warn("商品 {} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("商品 {} 庫存數量不足，無法購買。剩餘庫存 {}，欲購買數量 {}",
                        product.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 通過檢查，扣除商品庫存
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());


            // 計算總價錢
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

            // 轉換 BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        // 創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);


        return orderId;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);

        return order;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams, String username) {
        User user = userDao.getUserById(orderQueryParams.getUserId());

        if (user == null) {
            log.warn("查無使用者 UserId: {}", orderQueryParams.getUserId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!user.getEmail().equals(username)) {
            log.warn("不能查詢其他使用者 {} 的訂單，登入中的使用者: {}", user.getEmail(), username);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());

            order.setOrderItemList(orderItemList);
        }

        return orderList;
    }

    @Override
    public Integer countOrders(OrderQueryParams orderQueryParams) {
        return orderDao.countOrders(orderQueryParams);
    }
}
