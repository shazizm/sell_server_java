package com.imooc.sell.controller;


import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.service.PayService;
import com.lly835.bestpay.model.PayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller //因为返回的不是API用的 json 所以用Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/create") //ModelAndView 是 模板返回类型
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String, Object> map){ //这个参数为 payService 用的
        //1. 查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //2. 发起支付 （具体写在service里）。
        PayResponse payResponse = payService.create(orderDTO); //把订单 给 payService.create 去完成支付，返回 payResponse。

        //map.put("orderId", "111111"); //第一个参数可以直接穿 key value对，也可以传一个对象比如下行,使用的时候 在 html 里 直接 ${orderId}
        map.put("payResponse", payResponse); //也可以传 payResponse 对象，在 html 里 直接 ${payResponse.appId}
        map.put("returnUrl", returnUrl); //这就是传键值的方式。

        //返回时使用了 resources/templates/pay/create.frl  ，这里 ModelAndView 第一个传视图的模板名称，第二个传参数，是个map
        return new ModelAndView("pay/create", map); //这里使用了模板概念，当访问http://mishi.fantreal.com/sell/pay/create，就返回模板

    }

    @PostMapping("/notify") //../sell/pay/notify
    public void notify(@RequestBody String notifyData) {

        payService.notify(notifyData);

        //这里给微信返回一个修改订单状态完毕，微信就不一直给发消息了
        return new ModelAndView("pay/success");
    }
}
