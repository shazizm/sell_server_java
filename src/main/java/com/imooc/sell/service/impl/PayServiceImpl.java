package com.imooc.sell.service.impl;

import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.service.PayService;
import com.imooc.sell.utils.JsonUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    private static final String ORDER_NAME = "微信订餐订单"; //这个写死

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @Override
    public PayResponse create(OrderDTO orderDTO) {

        //用了和授权一样的方式，先在外部配置 (BestPayServiceImpl) 好了，就不用下面这样搞了,
//        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
//        bestPayService.setWxPayH5Config();
//
//        //和微信授权一样，需要先配置
//        //公众号 appid
//        //公众号 appSecret
//        //公众号 开通 微信支付，然后从商户平台 才能看到下面三项
//        //商户号 mchId
//        //商户密钥 mchKey
//        //商户证书路径 keyPath
        PayRequest payRequest = new PayRequest();

        payRequest.setOpenid(orderDTO.getBuyerOpenid()); //获取 openId
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue()); //获取 orderAmout， 因为setOrderAmout()参数要求double型，所以这里转一下
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信支付】发起支持 request={}", JsonUtil.toJson(payRequest));

        PayResponse payResponse = bestPayService.pay(payRequest); //TODO 重点标注 这里之后开通完商户平台 要再跑一下debug测试 这一步如果成功了 统一下单就算拿到了prepayId
        log.info("【微信支付】发起支付 response={}", JsonUtil.toJson(payResponse)); //将对象转成 json 输出log显示

        return payResponse;
    }

    @Override
    public PayResponse notify(@RequestBody String notifyData){ //notifyData 微信送来的xml字符串

        //异步通知安全性注意
        //1.验证签名 (验证是不是微信发来的)  【bestPayService 已经做了】
        //2.检查支付通知 是 什么状态，比如支付成功    【bestPayService 已经做了】
        //3.校验订单金额，和 微信返回的金额，是否一致
        //4.支付人（下单人 是否 是支付的人 ）

        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("【微信支付】 异步通知，payResponse={}", JsonUtil.toJson(payResponse));
        //上面是一个测试节点，可以在终端收到 logo.info 微信支付通知 的 xml 流水号

        //查询订单
        OrderDTO orderDTO = orderService.findOne(payResponse.getOrderId());

        //判断订单是否存在
        if(orderDTO == null){
            log.error("【微信支付】异步通知，订单不存在， order={}", payResponse.getOrderAmount());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //判断金额是否一致
        if(orderDTO.getOrderAmount().equals(payResponse.getOrderAmount())){
            log.error("【微信支付】异步通知，订单金额不一致，orderId={}, 微信通知金额={}， 系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderDTO.getOrderAmount());
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }

        //修改订单的支付状态为 已支付
        orderService.paid(orderDTO);

        return payResponse;
    }

}
