package com.maxdeng.springbootmall.service;

import com.maxdeng.springbootmall.dto.ProductQueryParams;
import com.maxdeng.springbootmall.dto.ProductRequest;
import com.maxdeng.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    Integer countProducts(ProductQueryParams productQueryParams);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);

}
