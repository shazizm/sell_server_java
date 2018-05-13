package com.imooc.sell.dataObject;

import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@DynamicUpdate
public class OrderMaster {

    @Id
    private String orderId;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;

    private String buyerOpenid;
//    订单总金额
    private BigDecimal orderAmount;
//    订单状态 默认是 新订单 0
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();
//    支付状态 默认是 未支付(等待支付) 0
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    private Date createTime;

    private Date updateTime;

//    @Transient //这个注解会在数据库对应的时候，忽略它没有key; 但是这样会乱，所以用 data transfer object = dto
//    private List<OrderDetail> orderDetailList;
}
