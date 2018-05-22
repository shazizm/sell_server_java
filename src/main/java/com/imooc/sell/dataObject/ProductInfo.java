package com.imooc.sell.dataObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imooc.sell.enums.ProductStatusEnum;
import com.imooc.sell.utils.EnumUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@DynamicInsert
@DynamicUpdate
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
//  商品状态 0正常 1下架 //新增商品时，默认上架,new ProductInfo的时候 这个值就默认有了
    private Integer productStatus = ProductStatusEnum.UP.getCode();
//  商品 和 类目 的关系就用 类目编号来关联
    private Integer categoryType;

    //创建时间 和 更新时间 老师说用的时候在加10-3的时候7：45时需要了
    private Date createTime;

    private Date updateTime;

    //10-3 12:20 增加一个取自己的枚举 code 的 方法 //TODO
    @JsonIgnore
    public ProductStatusEnum getProductStatusEnum(){
        return EnumUtil.getByCode(productStatus, ProductStatusEnum.class);
    }
}
