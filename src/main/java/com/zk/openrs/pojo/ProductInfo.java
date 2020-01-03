package com.zk.openrs.pojo;

import java.io.Serializable;

public class ProductInfo implements Serializable {
    int id;
    String productName;
    String productBindAccount;
    String productBindPassword;
    String productCurrentStatus;

    public ProductInfo(String productName, String productBindAccount, String productBindPassword, String productCurrentStatus) {
        this.productName = productName;
        this.productBindAccount = productBindAccount;
        this.productBindPassword = productBindPassword;
        this.productCurrentStatus = productCurrentStatus;
    }

    public ProductInfo(int id, String productName, String productBindAccount, String productBindPassword, String productCurrentStatus) {
        this.id = id;
        this.productName = productName;
        this.productBindAccount = productBindAccount;
        this.productBindPassword = productBindPassword;
        this.productCurrentStatus = productCurrentStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBindAccount() {
        return productBindAccount;
    }

    public void setProductBindAccount(String productBindAccount) {
        this.productBindAccount = productBindAccount;
    }

    public String getProductBindPassword() {
        return productBindPassword;
    }

    public void setProductBindPassword(String productBindPassword) {
        this.productBindPassword = productBindPassword;
    }

    public String getProductCurrentStatus() {
        return productCurrentStatus;
    }

    public void setProductCurrentStatus(String productCurrentStatus) {
        this.productCurrentStatus = productCurrentStatus;
    }
}
