package com.zk.openrs.pojo;

import lombok.Data;

@Data
public class Banner {
    int id;
    String imgPath;
    int isValidNow;

    public Banner(String imgPath, int isValidNow) {
        this.imgPath = imgPath;
        this.isValidNow = isValidNow;
    }

    public Banner(int id, String imgPath, int isValidNow) {
        this.id = id;
        this.imgPath = imgPath;
        this.isValidNow = isValidNow;
    }
}
