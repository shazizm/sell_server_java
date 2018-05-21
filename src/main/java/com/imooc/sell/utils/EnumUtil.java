package com.imooc.sell.utils;

import com.imooc.sell.enums.CodeEnum;

public class EnumUtil {

    //返回一个枚举T（这个枚举需要说明一下是 继承 extends CodeEnum），用 T = 泛型 表示,方法名就是 getByCode，方法的参数是 code 和 枚举enum的Class
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass){
        for(T each: enumClass.getEnumConstants()){ //TODO 写法看不懂 大概能猜出啥意思
            if(code.equals(each.getCode())){
                return each;
            }
        }
        return null;
    }
}
