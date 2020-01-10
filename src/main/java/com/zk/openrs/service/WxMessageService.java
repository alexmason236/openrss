package com.zk.openrs.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.zk.openrs.wechat.config.WxMaConfiguration;
import com.zk.openrs.wechat.config.WxMaProperties;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WxMessageService {
    @Autowired
    WxMaProperties properties;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public void sendOrderSuccess(String openId, String thing1, String account2, String content) throws WxErrorException {
        sendMsg(openId, thing1, account2, content, properties, df);

    }

    public void sendOrderFailMsg(String openId, String thing1, String account2, String content) throws WxErrorException {
        sendMsg(openId, thing1, account2, content, properties, df);
    }

    public void sendOrderCompleteMsg(String openId, String thing1, String account2, String content) throws WxErrorException {
        sendMsg(openId, thing1, account2, content, properties, df);
    }

    private static void sendMsg(String openId, String thing1, String account2, String content, WxMaProperties properties, SimpleDateFormat df) throws WxErrorException {
        WxMaService wxService = WxMaConfiguration.getMaService(properties.getConfigs().get(0).getAppid());
        WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
        wxMaSubscribeMessage.setToUser(openId);
        wxMaSubscribeMessage.setTemplateId(properties.getConfigs().get(0).getTemplate_id());
        wxMaSubscribeMessage.addData(new WxMaSubscribeMessage.Data("thing1", thing1))
                .addData(new WxMaSubscribeMessage.Data("amount2", String.valueOf(account2)))
                .addData(new WxMaSubscribeMessage.Data("date3", df.format(new Date())))
                .addData(new WxMaSubscribeMessage.Data("thing4", content));
        wxService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
    }
}
