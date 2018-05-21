package com.imooc.sell.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.sell.dataObject.OrderDetail;
import com.imooc.sell.dataObject.OrderMaster;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import com.imooc.sell.serializer.Date2LongSerializer;
import com.imooc.sell.utils.EnumUtil;
import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
//在每个里面加个这个有点累，可在application.yml里配置。@JsonInclude(JsonInclude.Include.NON_NULL) //加上这行 下面xxxList 为null值时，就不显示这个xxxList键了
public class OrderDTO {

    private String orderId;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;

    private String buyerOpenid;
    //    订单总金额
    private BigDecimal orderAmount;
    //    订单状态 默认是 新订单 0
    private Integer orderStatus;
    //    支付状态 默认是 未支付(等待支付) 0
    private Integer payStatus;

    @JsonSerialize(using = Date2LongSerializer.class) //这里 跟 /serializer 里重写方法有关，不懂
    private Date createTime;

    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    //在dto 里 增加 OrderMaster 要传输的数据
    List<OrderDetail> orderDetailList; //= new ArrayList<>(); //给个空字符串值，一遍orderDetailList为null时，也要现实成 []

    @JsonIgnore //加上这个注解，返回json时就会忽略这个方法
    public OrderStatusEnum getOrderStatusEnum(){
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum(){
        return EnumUtil.getByCode(payStatus, PayStatusEnum.class);
    }
}
