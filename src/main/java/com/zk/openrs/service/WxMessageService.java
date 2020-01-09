package com.zk.openrs.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.zk.openrs.wechat.config.WxMaConfiguration;
import com.zk.openrs.wechat.config.WxMaProperties;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WxMessageService {
    @Autowired
    WxMaProperties properties;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void send(String openId,String productName,String code) throws WxErrorException {
        final WxMaService wxService = WxMaConfiguration.getMaService(properties.getConfigs().get(0).getAppid());
        WxMaSubscribeMessage wxMaSubscribeMessage=new WxMaSubscribeMessage();
        wxMaSubscribeMessage.setToUser(openId);
        wxMaSubscribeMessage.setTemplateId(properties.getConfigs().get(0).getTemplate_id());
        wxMaSubscribeMessage.addData(new WxMaSubscribeMessage.Data("thing1",productName))
                .addData(new WxMaSubscribeMessage.Data("amount2",code))
                .addData(new WxMaSubscribeMessage.Data("date3",df.format(new Date())))
                .addData(new WxMaSubscribeMessage.Data("thing4","你的验证码为"+code));
        wxService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
    }
}
