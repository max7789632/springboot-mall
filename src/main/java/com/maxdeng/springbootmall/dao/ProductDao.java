package com.maxdeng.springbootmall.dao;

import com.maxdeng.springbootmall.dto.ProductQueryParams;
import com.maxdeng.springbootmall.dto.ProductRequest;
import com.maxdeng.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    Integer countProducts(ProductQueryParams productQueryParams);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void updateStock(Integer productId, Integer stock);

    void deleteProductById(Integer productId);
}
