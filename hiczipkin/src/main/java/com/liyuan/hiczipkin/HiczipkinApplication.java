package com.liyuan.hiczipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class HiczipkinApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiczipkinApplication.class, args);
    }

}
