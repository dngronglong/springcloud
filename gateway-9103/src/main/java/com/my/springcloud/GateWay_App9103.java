package com.my.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Created with IDEA
 * author:DongRonglong
 * Date:2018/8/10
 * Time:16:24
 */
@SpringCloudApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class GateWay_App9103 {
    public static void main(String[] args) {
        SpringApplication.run(GateWay_App9103.class,args);
    }

}
