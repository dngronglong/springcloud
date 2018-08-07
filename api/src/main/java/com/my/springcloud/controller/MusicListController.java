package com.my.springcloud.controller;


import com.my.springcloud.service.UserListService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/list")
public class MusicListController {

    @Resource
    private UserListService userListService;
    @RequestMapping("/userList")
    public Map u_list(@RequestParam Map<String,Object> map){
        map.put("userList",userListService.findSongListByUserId(Integer.parseInt(map.get("id").toString())));
        return map;
    }
}
