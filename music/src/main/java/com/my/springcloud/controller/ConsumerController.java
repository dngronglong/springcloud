package com.my.springcloud.controller;

import com.my.springcloud.beans.Result;
import com.my.springcloud.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private RestService restService;

    @ResponseBody
    @RequestMapping("/list")
    public Map search(@RequestParam Map<String, Object> map1){
        return restService.getRestData("/mic/search?source="+map1.get("source")+"&words="+map1.get("words")+"&pages="+map1.get("pages")+"&count="+map1.get("count"));
    }
    @RequestMapping("/index")
    public String index(){
        return "/index";
    }
}
