package com.liyuan.hicscserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class HicscserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(HicscserverApplication.class, args);
    }

}
