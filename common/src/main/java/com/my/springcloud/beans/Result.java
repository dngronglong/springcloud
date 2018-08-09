package com.my.springcloud.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.apache.http.HttpStatus;

/**
 * Created with IDEA
 * author:DongRonglong
 * Date:2018/8/9
 * Time:15:21
 */
public class Result extends HashMap implements Serializable {
    public Result() {
        put("code", 0);
        put("msg", "success");
    }

    public static Result error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知错误！");
    }

    public static Result error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static Result error(int code, String msg) {
        Result r = new Result();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static Result ok(String msg) {
        Result r = new Result();
        r.put("msg", msg);
        return r;
    }

    public static Result ok(Map<String, Object> map) {
        Result r = new Result();
        r.putAll(map);
        return r;
    }

    public static Result info(boolean success, String msg) {
        if (success) {
            if (StringUtils.hasText(msg)) {
                return Result.ok(msg);
            }
            return Result.ok();
        } else {
            if (StringUtils.hasText(msg)) {
                return Result.error(msg);
            }
            return Result.error();
        }
    }

    public static Result ok() {
        return new Result();
    }

    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
