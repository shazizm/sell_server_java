package com.imooc.sell.form;

import lombok.Data;

import java.math.BigDecimal;

@Data //自动增加get set啥的
public class ProductForm {

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
//在表单提交的时候这个值不是前端设置的    private Integer productStatus;
    //  商品 和 类目 的关系就用 类目编号来关联
    private Integer categoryType;
}
