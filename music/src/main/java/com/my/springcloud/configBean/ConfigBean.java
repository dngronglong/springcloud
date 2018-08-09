package com.my.springcloud.configBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created with IDEA
 * author:DongRonglong
 * Date:2018/8/8
 * Time:9:50
 */
@Configuration
public class ConfigBean {
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
