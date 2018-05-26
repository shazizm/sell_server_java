package com.imooc.sell.handler;

import com.imooc.sell.config.ProjectUrlConfig;
import com.imooc.sell.exception.ResponseBankException;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.exception.SellerAuthorizeException;
import com.imooc.sell.utils.ResultVOUtil;
import com.imooc.sell.viewObject.ResultViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice //TODO 这个注解干啥啊？
public class SellerExceptionHandler {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //SellerAuthorizeException()是在aspect切面里有抛出，拦截登陆异常,比如这里就是 检验发现没登录，自动 redirect 去下面 扫码 再登陆
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

    @ExceptionHandler(value = SellException.class) //捕获 SellExcepiton() 这类的异常
    @ResponseBody //TODO 上面并不是RESTController，所以这里加一个注解，来返回Json格式数据
    public ResultViewObject handlerSellerException(SellException e){ //TODO 这里不是很明白，可以从BuyerOrderController的create开始看商品不存在的throw
        return ResultVOUtil.error(e.getCode(),e.getMessage());
    }

    //这是一个例子并没有用到，可以在 orderServiceImpl 的 create 的 商品id找不到出 抛出，这里捕获。
    @ExceptionHandler(value = ResponseBankException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //status非200的问题就这么解决，参数是一个枚举Enum
    public void handlerResponseBankException(){

    }

}
