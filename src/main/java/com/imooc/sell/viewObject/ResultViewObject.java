package com.imooc.sell.viewObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


//http 请求返回的最外层对象
@Data
//在每个dto里加这个，比较累。 @JsonInclude(JsonInclude.Include.NON_NULL) //加上这行 下面xxxList 为null值时，就不显示这个xxxList键了
public class ResultViewObject<T> {

//  错误码
    private Integer code;
//  提示信息
    private String msg;
//    返回的具体内容
    private  T data;
}
