package com.zk.openrs.wechat.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.zk.openrs.pojo.WechatUser;
import com.zk.openrs.service.UserService;
import com.zk.openrs.wechat.config.WxMaConfiguration;
import com.zk.openrs.wechat.config.WxMaProperties;
import com.zk.openrs.wechat.utils.JsonUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/user")
public class WxMaUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    WxMaProperties properties;
    @Autowired
    UserService userService;

    /**
     * 登陆接口
     */
    @GetMapping("/login")
    public String login( String code,String signature, String rawData, String encryptedData, String iv) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(signature) || StringUtils.isBlank(rawData) || StringUtils.isBlank(encryptedData) || StringUtils.isBlank(iv)) {
            return "empty jscode or other ";
        }

        final WxMaService wxService = WxMaConfiguration.getMaService(properties.getConfigs().get(0).getAppid());

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
//            this.logger.info(session.getSessionKey());
//            this.logger.info(session.getOpenid());
            String sessionKey=session.getSessionKey();
            if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
                return "user check failed";
            }
            WechatUser wechatUser=userService.getByOpenId(session.getOpenid());
            if (wechatUser==null){
                logger.info("user not exist, do REGIST");
                WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
                //TODO 可以增加自己的逻辑，关联业务相关数据
                WechatUser addedUser=new WechatUser(session.getOpenid(),20,"123456789",userInfo.getNickName(),userInfo.getGender(),userInfo.getCity(),userInfo.getProvince());
                userService.addUser(addedUser);
                logger.info(JsonUtils.toJson(addedUser));
                return JsonUtils.toJson(addedUser);
            }
            return JsonUtils.toJson(wechatUser);

        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            return e.toString();
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
//    @GetMapping("/info")
//    public String info(@PathVariable String appid, String sessionKey,
//                       String signature, String rawData, String encryptedData, String iv) {
//        final WxMaService wxService = WxMaConfiguration.getMaService(appid);
//
//        //用户信息校验
//        if (!wxService.getUserService().checkUserInfo(sessionKey.trim(), rawData.trim(), signature.trim())) {
//            return "user check failed";
//        }
//        logger.info(String.valueOf(encryptedData.length()));
//        logger.info(String.valueOf(iv.length()));
//        // 解密用户信息
//        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey.trim(), encryptedData.trim(), iv.trim());
//
//        return JsonUtils.toJson(userInfo);
//    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    public String phone(@PathVariable String appid, String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(phoneNoInfo);
    }

}
