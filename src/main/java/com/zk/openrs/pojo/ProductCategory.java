package com.zk.openrs.pojo;

import lombok.Data;

@Data
public class ProductCategory {
    int id;
    String categoryName;
    String categoryBindPicturePath;
    float price;

    public ProductCategory(String categoryName, String categoryBindPicturePath) {
        this.categoryName = categoryName;
        this.categoryBindPicturePath = categoryBindPicturePath;
    }

    public ProductCategory(int id, String categoryName, String categoryBindPicturePath) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryBindPicturePath = categoryBindPicturePath;
    }

    public ProductCategory(String categoryName, String categoryBindPicturePath, float price) {
        this.categoryName = categoryName;
        this.categoryBindPicturePath = categoryBindPicturePath;
        this.price = price;
    }

    public ProductCategory(int id, String categoryName, String categoryBindPicturePath, float price) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryBindPicturePath = categoryBindPicturePath;
        this.price = price;
    }
}
