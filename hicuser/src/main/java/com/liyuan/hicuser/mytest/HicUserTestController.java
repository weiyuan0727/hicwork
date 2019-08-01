package com.liyuan.hicuser.mytest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by weiyuan on 2019/7/5/005.
 */
@RestController
@RefreshScope
public class HicUserTestController {
    @Value("${server.port}")
    private String port;
    //@Value(value = "${hello}")
    private String profile;

    //@Value(value = "${common}")
    private String common;

    @Autowired(required=false)
    private FeginTestService feginTestService;

    //@HystrixCommand 测试仪表盘用 实际不需要
    @HystrixCommand
    @RequestMapping("/hi")
    public String sayHi(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String t = request.getParameter("access_token");
        System.out.println("hi");
        return "你好，游客，这里是:" + port;
    }

    @RequestMapping("/profile")
    public String profile(){
        System.out.println(this.profile);
        return "private:"+this.profile+" common:"+this.common;
    }

    @RequestMapping("/testfegin")
    @ResponseBody
    public String testFegin() {
        System.out.println(1111);
        return feginTestService.testFegin();
    }
}
