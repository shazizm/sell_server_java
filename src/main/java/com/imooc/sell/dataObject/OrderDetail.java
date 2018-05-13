package com.imooc.sell.dataObject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class OrderDetail {

    @Id
    private String detailId;

    private String orderId;

    private String productId; // create 时，前端直传

    private String productName;

    private BigDecimal productPrice;

    private Integer productQuantity; // create 时，前端直传

    private String productIcon;

    //createTime 暂时没用到，先不加了
}
