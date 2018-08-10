package com.my.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created with IDEA
 * author:DongRonglong
 * Date:2018/8/10
 * Time:11:49
 */
@SpringBootApplication
@EnableEurekaServer
public class Eureka_App7001 {
    public static void main(String[] args) {
        SpringApplication.run(Eureka_App7001.class,args);
    }
}
