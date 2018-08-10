package com.my.springcloud.controller;

import com.my.springcloud.beans.Result;
import com.my.springcloud.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * author:DongRonglong
 * Date:2018/8/8
 * Time:10:03
 */
@Controller
@RequestMapping("/mic")
public class ConsumerController {
    public static final String MUSIC_LIST_URL = "http://api-8080/mic/search";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpHeaders headers;
    @ResponseBody
    @RequestMapping("/list")
    public Map search(@RequestParam Map<String, Object> map1){
        System.out.println(headers.get("Authorization"));
        String url=MUSIC_LIST_URL+"?source="+map1.get("source")+"&words="+map1.get("words")+"&pages="+map1.get("pages")+"&count="+map1.get("count");
        System.out.println(url);
        Map map=restTemplate.exchange(url,HttpMethod.GET,new HttpEntity<Object>(headers),HashMap.class).getBody();
//        Map map=restTemplate.getForObject(MUSIC_LIST_URL+"?source="+map1.get("source")+"&words="+map1.get("words")+"&pages="+map1.get("pages")+"&count="+map1.get("count"),HashMap.class);
        return map;
//        return restService.getRestData("/mic/search?source="+map1.get("source")+"&words="+map1.get("words")+"&pages="+map1.get("pages")+"&count="+map1.get("count"));
    }
    @RequestMapping("/index")
    public String index(){
        return "/index";
    }
}
