package com.imooc.sell.dataObject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class ProductInfo {

    @Id //商品id 是随机生成的字符串
    private String productId;
//  商品名称
    private String productName;
//  单价
    private BigDecimal productPrice;
//  库存
    private Integer productStock;
//  描述
    private String productDescription;
//  小图
    private String productIcon;
//  商品状态 0正常 1下架
    private Integer productStatus;
//  商品 和 类目 的关系就用 类目编号来关联
    private Integer categoryType;

    //创建时间 和 更新时间 老师说用的时候在加
}
