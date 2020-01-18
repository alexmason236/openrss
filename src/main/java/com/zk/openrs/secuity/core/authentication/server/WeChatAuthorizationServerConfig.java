package com.zk.openrs.secuity.core.authentication.server;

import com.zk.openrs.secuity.core.properties.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import javax.annotation.Resource;


@Configuration
@EnableAuthorizationServer
public class WeChatAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Resource
    private SecurityProperties securityProperties;
    @Resource
    private UserDetailsService wechatUserDetailService;
    public void configure(AuthorizationServerSecurityConfigurer security)  {
        security.tokenKeyAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        builder.withClient(securityProperties.getClintId()).secret(securityProperties.getClientSecret()).authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .accessTokenValiditySeconds(securityProperties.getTokenValiditySeconds())
                .refreshTokenValiditySeconds(securityProperties.getRefreshTokenValiditySeconds())
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.userDetailsService(wechatUserDetailService);
    }
}
