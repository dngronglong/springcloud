package com.my.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created with IDEA
 * author:DongRonglong
 * Date:2018/8/7
 * Time:17:02
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class Api_App8080 {

    public static void main(String[] args) {
        SpringApplication.run(Api_App8080.class,args);
    }
}
