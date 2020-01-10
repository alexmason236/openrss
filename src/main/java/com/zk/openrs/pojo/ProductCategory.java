package com.zk.openrs.pojo;

import lombok.Data;

@Data
public class ProductCategory {
    int id;
    String categoryName;
    String categoryBindPicturePath;

    public ProductCategory(String categoryName, String categoryBindPicturePath) {
        this.categoryName = categoryName;
        this.categoryBindPicturePath = categoryBindPicturePath;
    }

    public ProductCategory(int id, String categoryName, String categoryBindPicturePath) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryBindPicturePath = categoryBindPicturePath;
    }
}
