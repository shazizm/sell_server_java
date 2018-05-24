package com.imooc.sell.aspect;

import com.imooc.sell.constant.CookieConstant;
import com.imooc.sell.constant.RedisConstant;
import com.imooc.sell.exception.SellerAuthorizeException;
import com.imooc.sell.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Aspect //AOP 切面写法，类似nodejs 的中间件
@Component
@Slf4j
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //controller下以Seller开头的下面的所有方法*(..), 并且&& 不包括！SellerUserController下面所有的方法*(..).
    @Pointcut("execution(public * com.imooc.sell.controller.Seller*.*(..))" +
    "&& !execution(public * com.imooc.sell.controller.SellerUserController.*(..))")
    public void verify(){}

    @Before("verify()") //在verify()这个切入点之前 先执行doVerify()
    public void doVerify(){
        //获取http的request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //查询cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie == null){
            log.warn("【登陆校验】Cookie中查不到token");
            throw new SellerAuthorizeException(); //老师说 这里是抛出一个 异常，而不是直接跳转，在异常里再去处理异常（跳转）
        }

        //去redis里查询 取出token值
        String tokenValue = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
        //判断 tokenValue 为空，或者没有这个 值，都返回warn
        if(StringUtils.isEmpty(tokenValue)){
            log.warn("【登陆校验】Redis 中查不到token");
            throw new SellerAuthorizeException();
        }
    }
}
