package com.my.springcloud.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * Created with IDEA
 * author:DongRonglong
 * Date:2018/8/10
 * Time:9:55
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //    @Bean
//    UserDetailsService customUserService(){
//        return new CustomUserService();
//    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserService());
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("USER");
//        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("admin").password(new BCryptPasswordEncoder().encode("123456")).roles("USER");
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//配置安全策略
                .antMatchers("/", "/mic").permitAll()//定义/请求不需要验证
//                .anyRequest().authenticated()//其余的所有请求都需要验证
                .and()
                .logout()
                .permitAll()//定义logout不需要验证
                .and()
                .formLogin();//使用form表单登录
    }
}
