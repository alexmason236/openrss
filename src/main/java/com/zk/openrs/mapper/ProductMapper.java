package com.zk.openrs.mapper;

import com.zk.openrs.pojo.Banner;
import com.zk.openrs.pojo.Order;
import com.zk.openrs.pojo.ProductCategory;
import com.zk.openrs.pojo.ProductInfo;

import java.util.List;

public interface ProductMapper {
    ProductInfo getById(int pid);

    void addProduct(ProductInfo productInfo);

    List<ProductInfo> getAllProduct();

    int createOrder(Order order);

    List<ProductInfo> getProductByProductName(String productName);

    ProductInfo getProductByPhoneNumberAndProductNameAndProductStatus(String fromMobile, String productName, String productStatus);

    Order getOrderByProductNameAndOrderStatus(int productId, String orderStatus);

    void updateProductStatus(int productId, String productStatus);

    void updateOrderStatus(int orderId, String orderStatus);

    void addCategory(ProductCategory productCategory);

    List<ProductCategory> getAllCategory();

    Order getOrderByOrderId(int id);

    List<ProductCategory> getCategoryBiCid(int categoryId);

    List<ProductInfo> getAvailableProductByCategoryId(int categoryId);

    List<Order> getOrdersByUser(String openId);

    void addBanner(Banner banner);

    List<Banner> getBanner();
}
