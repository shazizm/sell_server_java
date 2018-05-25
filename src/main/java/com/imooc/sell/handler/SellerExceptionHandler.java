package com.imooc.sell.handler;

import com.imooc.sell.config.ProjectUrlConfig;
import com.imooc.sell.exception.SellerAuthorizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice //TODO 这个注解干啥啊？
public class SellerExceptionHandler {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //拦截登陆异常,比如这里就是 检验发现没登录，自动 redirect 去下面 扫码 再登陆
    //http://mishi.fantreal.com/sell/wechat/qrAuthorize?returnUrl=http://mishi.fantreal.com/sell/seller/login
    @ExceptionHandler(value = SellerAuthorizeException.class) //捕获 SellerAuthorizeException() 这类的异常
    public ModelAndView handlerAuthorizeException(){
        return new ModelAndView("redirect:"
                .concat(projectUrlConfig.getWechatOpenWebAuthorize())
                .concat("/sell/wechat/qrAuthorize")
                .concat("?state=")
                .concat(projectUrlConfig.getSell())
                .concat("/sell/seller/login")
        );
    }
}
