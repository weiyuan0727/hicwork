package com.liyuan.hiczuul.myzuul.hystrix;

import com.netflix.appinfo.RefreshableAmazonInfoProvider;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by weiyuan on 2019/7/14/014.
 */
@Component
public class MyZuulFallbackProvider implements FallbackProvider {
    // 表明是为哪个微服务提供回退，*表示为所有微服务提供回退
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            //返回响应的HTTP状态代码
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }
            // 返回HTTP状态代码
            @Override
            public int getRawStatusCode() throws IOException {
                return this.getStatusCode().value();
            }
            // 返回响应的HTTP状态文本
            @Override
            public String getStatusText() throws IOException {
                return this.getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {
            }
            // 响应体
            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream((route+"服务不可用！"+cause.getMessage()).getBytes());
            }
            // 设置header
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers=new HttpHeaders();
                MediaType mt=new MediaType("application","json", Charset.forName("UTF-8"));
                headers.setContentType(mt);
                return headers;
            }
        };

    }
}
