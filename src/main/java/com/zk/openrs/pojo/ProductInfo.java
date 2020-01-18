package com.zk.openrs.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProductInfo implements Serializable {
    int id;
    String productName;
    @JsonIgnore
    String productBindAccount;
    @JsonIgnore
    String productBindPassword;
    String productCurrentStatus;
    String productBindPicturePath;
    String productBelongto;
    int productCategoryId;



    public ProductInfo(String productName) {
        this.productName = productName;
    }


    public ProductInfo(String productName, String productBindAccount, String productBindPassword, String productCurrentStatus, String productBindPicturePath, String productBelongto, int productCategoryId) {
        this.productName = productName;
        this.productBindAccount = productBindAccount;
        this.productBindPassword = productBindPassword;
        this.productCurrentStatus = productCurrentStatus;
        this.productBindPicturePath = productBindPicturePath;
        this.productBelongto = productBelongto;
        this.productCategoryId = productCategoryId;
    }

    public ProductInfo(int id, String productName, String productBindAccount, String productBindPassword, String productCurrentStatus, String productBindPicturePath, String productBelongto, int productCategoryId) {
        this.id = id;
        this.productName = productName;
        this.productBindAccount = productBindAccount;
        this.productBindPassword = productBindPassword;
        this.productCurrentStatus = productCurrentStatus;
        this.productBindPicturePath = productBindPicturePath;
        this.productBelongto = productBelongto;
        this.productCategoryId = productCategoryId;
    }
}
