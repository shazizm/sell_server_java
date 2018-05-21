package com.imooc.sell.enums;

//public interface CodeEnum<T> { //TODO 泛型？？这里的接口使用方式值得学习,老师让 OrderStatusEnum 和 PayStatusEnum 分别实现这个接口
//    T getCode();
//}

public interface CodeEnum { //TODO 泛型？？这里的接口使用方式值得学习,老师让 OrderStatusEnum 和 PayStatusEnum 分别实现这个接口
    Integer getCode(); //因为我知道getCode返回的是int型，所以就不用上面的泛型了
}
