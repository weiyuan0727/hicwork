package com.liyuan.hicsys.mytest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by weiyuan on 2019/7/6/006.
 */
@FeignClient(value = "hicuser",fallback = SchedualServiceHiImpl.class,configuration = com.liyuan.hicsys.config.OAuth2FeignRequestInterceptor2.class)
public interface SchedualServiceHi {
    @RequestMapping("/hi")
    public String testFeign();

}
