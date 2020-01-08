package com.zk.openrs.mapper;

import com.zk.openrs.pojo.Order;
import com.zk.openrs.pojo.ProductInfo;

import java.util.List;

public interface ProductMapper {
    ProductInfo getById(int pid);
    void addProduct(ProductInfo productInfo);
    List<ProductInfo> getAllProduct();
    int createOrder(Order order);
    List<ProductInfo> getProductByProductName(String productName);
    void updateThisProduct(ProductInfo productInfo);
    ProductInfo getProductByPhoneNumberAndProductNameAndProductStatus(String fromMobile, String productName, String productStatus);

    Order getOrderByProductNameAndOrderStatus(int productId, String orderStatus);
}
