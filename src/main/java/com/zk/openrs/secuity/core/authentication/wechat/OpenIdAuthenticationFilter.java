package com.zk.openrs.secuity.core.authentication.wechat;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenIdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SECURITY_OPENID_KEY = "openid";
    private String openidParameter = SECURITY_OPENID_KEY;
    private boolean postOnly = true;

    public OpenIdAuthenticationFilter() {
        super(new AntPathRequestMatcher("/authenticate/openid", "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String openid = this.obtainOpenid(request);
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
    protected String obtainOpenid(HttpServletRequest request) {
        return request.getParameter(this.openidParameter);
    }

    protected void setDetails(HttpServletRequest request, OpenIdAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }


    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public String getOpenidParameter() {
        return openidParameter;
    }

    public void setOpenidParameter(String openidParameter) {
        this.openidParameter = openidParameter;
    }
}
