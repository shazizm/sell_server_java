package com.imooc.sell.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MathUtil {

    private static final Double MONEY_RANGE = 0.01;

    //比较2个金额是否相等，以为数字对比很操蛋，这里用两值差小于金额最小精度 就判断为相等。
    public static Boolean equals(Double d1, Double d2){
        Double result = Math.abs(d1 - d2);
        log.info("【微信支付】 判断差值为，result={}", result);
        log.info("【微信支付】判断范围为，range={}", MONEY_RANGE);
        if(result < MONEY_RANGE) {
            return true;
        }else{
            return false;
        }
    }
}
