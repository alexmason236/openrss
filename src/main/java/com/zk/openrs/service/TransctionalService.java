package com.zk.openrs.service;

import com.zk.openrs.pojo.BuyPojo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TransctionalService {
    @Resource
    private ProductService productService;

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> doBuy(String formId, List<BuyPojo> buyPojos, Authentication authentication) throws Exception {
        List<Map<String, Object>> list=new ArrayList<>();
        for (int i = 0; i < buyPojos.size(); i++) {
            String categoryName = productService.getCategoryBiCid(buyPojos.get(i).getGoodsId()).get(0).getCategoryName();
            Map<String, Object> objectMap = productService.createOrder(formId, buyPojos.get(i).getNumber(), categoryName, authentication);
            list.add(objectMap);
        }
        return list;
    }
}
