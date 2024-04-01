package com.maxdeng.springbootmall.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotEmpty // 不可以是空集合
    @Valid
    private List<BuyItem> buyItemList;
}
