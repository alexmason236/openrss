package com.zk.openrs.secuity.core.authentication.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.zk.openrs.wechat.config.WxMaConfiguration;
import com.zk.openrs.wechat.config.WxMaProperties;
import lombok.SneakyThrows;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenIdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SECURITY_CODE_KEY = "code";
    private String codeParameter = SECURITY_CODE_KEY;
    private boolean postOnly = true;

    public OpenIdAuthenticationFilter() {
        super(new AntPathRequestMatcher("/authenticate/code", "POST"));
    }


    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        logger.info("进入filter，从此处拿openId");
        WxMaProperties properties = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean(WxMaProperties.class);
        WxMaService wxService = WxMaConfiguration.getMaService(properties.getConfigs().get(0).getAppid());
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String code = this.obtainCode(request);
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            if (openid == null) {
                openid = "";
            }

            if (openid == null) {
                openid = "";
            }

            openid = openid.trim();
            OpenIdAuthenticationToken authRequest = new OpenIdAuthenticationToken(openid);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }


    @Nullable
    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(this.codeParameter);
    }

    protected void setDetails(HttpServletRequest request, OpenIdAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }


    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public String getCodeParameter() {
        return codeParameter;
    }

    public void setCodeParameter(String codeParameter) {
        this.codeParameter = codeParameter;
    }
}
