package com.zk.openrs.secuity.core.authentication.wechat;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class OpenIdAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Resource
    private AuthenticationSuccessHandler weChatAuthenticateSuccessHandler;
    @Resource
    private AuthenticationFailureHandler wechatAuthenctiationFailureHandler;
    @Resource
    private UserDetailsService wechatUserDetailService;

    @Override
    public void configure(HttpSecurity builder) {
        OpenIdAuthenticationFilter openIdAuthenticationFilter=new OpenIdAuthenticationFilter();
        openIdAuthenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        openIdAuthenticationFilter.setAuthenticationSuccessHandler(weChatAuthenticateSuccessHandler);
        openIdAuthenticationFilter.setAuthenticationFailureHandler(wechatAuthenctiationFailureHandler);
        OpenIdAuthenticateProvider authenticationProvider=new OpenIdAuthenticateProvider();
        authenticationProvider.setUserDetailsService(wechatUserDetailService);
        builder.authenticationProvider(authenticationProvider)
                .addFilterAfter(openIdAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
