package com.liyuan.hiclogio.oauth.config;

import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by weiyuan on 2019/7/21/021.
 */
public class OAuth2FeignRequestInterceptor2 extends OAuth2FeignRequestInterceptor {
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_TOKEN_TYPE = "Bearer";

    public OAuth2FeignRequestInterceptor2(OAuth2ClientContext oAuth2ClientContext,
                                          OAuth2ProtectedResourceDetails resource) {
        super(oAuth2ClientContext, resource);
    }

    @Autowired
    private OAuth2ClientContext context;

    /**
     * fegin调用token传递的关键 注释的部分也可以
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        OAuth2AccessToken oAuth2AccessToken = context.getAccessToken();
        String token = context.getAccessToken().getValue();
        if (oAuth2AccessToken != null && token != null
                && OAuth2AccessToken.BEARER_TYPE.equalsIgnoreCase(context.getAccessToken().getTokenType())) {
            template.header("Authorization",
                    String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, context.getAccessToken().getValue()));
        }
/*
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String auth = request.getHeader("Authorization");
        String token = request.getParameter("access_token");

        String access_token = auth == null ? token : auth;
        template.header(HttpHeaders.AUTHORIZATION, access_token);*/
    }
}
