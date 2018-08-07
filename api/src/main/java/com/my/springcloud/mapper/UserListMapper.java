package com.my.springcloud.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component("UserListMapper")
public interface UserListMapper {
    List<Map> findSongListByUserId(@Param("id") int id);
}
