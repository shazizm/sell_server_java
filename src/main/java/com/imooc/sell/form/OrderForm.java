package com.imooc.sell.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

//form 文件夹下的文件都是 表单验证，这个文件就是 订单的表单验证
@Data
public class OrderForm {

    @NotEmpty(message = "姓名必填")
    private  String name;

    @NotEmpty(message = "手机号必填")
    private String phone;

    @NotEmpty(message = "地址必填")
    private String address;

    @NotEmpty(message = "openid必填")
    private String openid;

    @NotEmpty(message = "购物车不能为空")
    private String items; //这里传过来 是 字符串 ，但是其实是一个 json ，在用之前要处理一下

}
