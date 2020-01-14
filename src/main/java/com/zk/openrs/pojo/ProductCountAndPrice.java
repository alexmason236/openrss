package com.zk.openrs.pojo;

import lombok.Data;

@Data
public class ProductCountAndPrice {
    int availableCount;
    float price;

    public ProductCountAndPrice(int availableCount, float price) {
        this.availableCount = availableCount;
        this.price = price;
    }
}
