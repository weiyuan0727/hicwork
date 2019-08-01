package com.liyuan.hicsys.mytest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by weiyuan on 2019/7/21/021.
 */
@FeignClient(value = "hiclogio",configuration = com.liyuan.hicsys.config.OAuth2FeignRequestInterceptor2.class)
public interface FeginTestService {
    @RequestMapping("/zip")
    public String testFegin();
    @RequestMapping("/profile")
    public String profie();
}
