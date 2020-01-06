package com.zk.openrs.mapper;

import com.zk.openrs.pojo.ProductInfo;

import java.util.List;

public interface ProductMapper {
    ProductInfo getById(int pid);
    void addProduct(ProductInfo productInfo);
    public List<ProductInfo> getAllProduct();
}
