package com.imooc.sell.utils;

import java.util.Random;

public class GenKeyUtil {
    //生成唯一主键
    //格式：时间 + 随机数
    //这几加了 synchronized 是为了防止 多线程 时，随机数会重复
    public static synchronized String genUniqueKey(){

        Random random = new Random(); //当前随机数

        //System.currentTimeMillis(); //当前毫秒数

        Integer number = random.nextInt(900000) + 100000;  // 这样写 就是 六位随机数

        return System.currentTimeMillis() + String.valueOf(number); //拼接成字符串返回

    }
}
