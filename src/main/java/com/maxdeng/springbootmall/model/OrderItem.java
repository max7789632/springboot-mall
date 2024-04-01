package com.maxdeng.springbootmall.model;

import lombok.Data;

@Data
public class OrderItem {

    private Integer oderItemId;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Integer amount;

    private String productName;
    private String imageUrl;
}
