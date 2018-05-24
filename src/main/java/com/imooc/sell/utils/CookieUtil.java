package com.imooc.sell.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

// cookie 工具类
public class CookieUtil {

    public static void set(HttpServletResponse response,
                           String name,
                           String value,
                           int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/"); //目录一般不变，就默认了
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void get(){

    }
}
