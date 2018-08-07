package com.my.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created with IDEA
 * author:DongRonglong
 * Date:2018/8/7
 * Time:16:56
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class Music_App80 {

    public static void main(String[] args) {
        SpringApplication.run(Music_App80.class,args);
    }
}
