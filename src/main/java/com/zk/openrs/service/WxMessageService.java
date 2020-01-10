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
    final WxMaService wxService = WxMaConfiguration.getMaService(properties.getConfigs().get(0).getAppid());

    public void send(String openId, String productName, int rentalTime, String code) throws WxErrorException {

        WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
        wxMaSubscribeMessage.setToUser(openId);
        wxMaSubscribeMessage.setTemplateId(properties.getConfigs().get(0).getTemplate_id());
        wxMaSubscribeMessage.addData(new WxMaSubscribeMessage.Data("thing1", productName))
                .addData(new WxMaSubscribeMessage.Data("amount2", String.valueOf(rentalTime)))
                .addData(new WxMaSubscribeMessage.Data("date3", df.format(new Date())))
                .addData(new WxMaSubscribeMessage.Data("thing4", "你的验证码为" + code));
        wxService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);

    }

    public void sengFailMsg(String openId, String thing1, String account2, String content) throws WxErrorException {
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
