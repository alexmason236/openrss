package com.zk.openrs.secuity.core.authentication.server;

import com.zk.openrs.secuity.core.authentication.wechat.OpenIdAuthenticationSecurityConfig;
import com.zk.openrs.secuity.core.properties.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.annotation.Resource;

@Configuration
@EnableResourceServer
public class WeChatResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Resource
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;
    @Resource
    private SecurityProperties securityProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers(securityProperties.getAuthorizedUrl(), "/oauth/token", "/wx/user/login", "/product/getTestMsg", "/product/getAllCategory")
                .antMatchers("/product/buyProduct","/product/productAvailableCountAndPrice")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .apply(openIdAuthenticationSecurityConfig)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
}
