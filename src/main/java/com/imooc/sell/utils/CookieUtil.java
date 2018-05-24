package com.imooc.sell.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

// cookie 工具类
public class CookieUtil {

    //设置 cookie
    public static void set(HttpServletResponse response,
                           String name,
                           String value,
                           int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/"); //目录一般不变，就默认了
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    //获取 cookie
    public static Cookie get(HttpServletRequest request,
                           String name){
        //这里获得是一个cookie字符串，在下面写一个readCookieMap私有的方法
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if(cookieMap.containsKey(name)){
            return cookieMap.get(name);
        }else{
            return null;
        }

    }

    //将 cookie 封装成 Map
    //把 request.getCookies() 转成一个 Map，方便上面用 Map.containsKey(name)来查询，需要的key对应的cookie
    private static Map<String, Cookie> readCookieMap(HttpServletRequest request){
        Map<String, Cookie> cookieMap = new HashMap<>();

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie: cookies){  //java 的 for
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
