package com.imooc.sell.controller;

import com.imooc.sell.config.ProjectUrlConfig;
import com.imooc.sell.constant.CookieConstant;
import com.imooc.sell.constant.RedisConstant;
import com.imooc.sell.dataObject.SellerInfo;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.service.SellerService;
import com.imooc.sell.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seller")
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid,
                              HttpServletResponse response,
                              Map<String, Object> map){

        //1. openid 去和 数据库 里的 数据匹配
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        if(sellerInfo == null){
            map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }

        //2. 设置 token 至 redis （token 保存在 redis 里，在这里做校验）
        String token = UUID.randomUUID().toString(); //TODO 这个UUID什么鬼 (Universally Unique Identifier)
        Integer expire = RedisConstant.EXPIRE;

        // set(key，value, 过期时间TTL，过期时间单位) 一般都设置过期时间，要不越存越多
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), openid, expire, TimeUnit.SECONDS);

        //3. 设置 token 至 cookie （token setCookie 是让用户浏览器 保存 token）

        //如果设置cookie 需要 增加一个 HttpServletResponse 的参数,正常设置cookie，这里用一个工具类来代替
//        Cookie cookie = new Cookie("token", token);
//        cookie.setPath("/");
//        cookie.setMaxAge(7200);
//        response.addCookie(cookie);

        //自定义的工具类 （response，key，value，过期值）
        CookieUtil.set(response, CookieConstant.TOKEN, token, expire); //这个值没有token_前缀，但是redis里的有，校验的时候不知道怎么弄

        //return new ModelAndView("redirect:/sell/seller/order/list"); 这么写跳转后 就多一个sell，建议redirect 都用绝对地址
        return new ModelAndView("redirect:" + projectUrlConfig.getSell() + "/sell/seller/order/list");

    }

    @GetMapping("/logout")
    public void logout(){

    }
}
