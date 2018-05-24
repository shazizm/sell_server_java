package com.imooc.sell.constant;

//redis 常量

public interface RedisConstant {

    String TOKEN_PREFIX = "token_%s"; //TODO 这里老师说是为了存储的tokenkey值，是以"token_"开始的。

    Integer EXPIRE = 7200; //2小时
}
