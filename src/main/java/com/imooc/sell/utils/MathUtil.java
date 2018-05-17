package com.imooc.sell.utils;

public class MathUtil {

    private static final Double MONEY_RANGE = 0.01;

    //比较2个金额是否相等，以为数字对比很操蛋，这里用两值差小于金额最小精度 就判断为相等。
    public static Boolean equals(Double d1, Double d2){
        Double result = Math.abs(d1 - d2);
        if(result < MONEY_RANGE) {
            return true;
        }else{
            return false;
        }
    }
}
