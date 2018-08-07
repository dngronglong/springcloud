package com.my.springcloud.service.impl;


import com.my.springcloud.mapper.UserListMapper;
import com.my.springcloud.service.UserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserListServiceImpl implements UserListService {

    @Autowired
    private UserListMapper userListMapper;

    @Override
    public List<Map> findSongListByUserId(int id) {
        return userListMapper.findSongListByUserId(id);
    }
}
