package com.liyuan.hicsys.mytest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by weiyuan on 2019/7/5/005.
 */
@RestController
@RefreshScope
public class HicLogioTestController {

    private String profile;

    private String common;

    private String common2;
    @Autowired
    private RestTemplate restTemplate;
    //如果required = true 编译器报错，可以无视。 因为这个Bean是在程序启动的时候注入的，编译器感知不到，所以报错。
    //required = false 编译的时候就不会报错了
    @Autowired(required = false)
    SchedualServiceHi schedualServiceHi;

    @HystrixCommand(fallbackMethod = "hiError")
    @RequestMapping("/tohiuser")
    @ResponseBody
    public String toHicUser(@Param("access_token") String token) {
        System.out.println("toHicUser");
        String re = restTemplate.getForObject("http://HICUSER/hi?access_token="+token, String.class);
        return re;
    }

    @RequestMapping("/testfegin")
    @ResponseBody
    public String testFeign(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String t = request.getParameter("access_token");
        System.out.println("testFeign");
        String re = schedualServiceHi.testFeign();
        return re;
    }

    @RequestMapping("/profile")
    public String profile() {
        System.out.println(this.profile);
        return "private:" + this.profile + " common:" + this.common + " common-dev:" + common2;
    }

    public String hiError() {
        return "不好意思。。异常了。。";
    }

    @RequestMapping("/zip")
    public String testZipkin() {
        return "zip";
    }

}
