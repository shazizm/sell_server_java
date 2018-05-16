package com.imooc.sell.service.impl;

import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.service.PayService;
import com.imooc.sell.utils.JsonUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    private static final String ORDER_NAME = "微信订餐订单"; //这个写死

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Override
    public void create(OrderDTO orderDTO) {

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
        log.info("【微信支付】request={}", JsonUtil.toJson(payRequest));

        PayResponse payResponse = bestPayService.pay(payRequest); //TODO 重点标注 这里之后开通完商户平台 要再跑一下debug测试 这一步如果成功了 统一下单就算拿到了prepayId
        log.info("【微信支付response】={}", JsonUtil.toJson(payResponse));
    }
}
