package com.imooc.sell.dataObject;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
//自动更新update时的时间
@DynamicInsert
@DynamicUpdate
//Data 里 自动get、set、toString
@Data
public class ProductCategory {

//    类目id
    @Id
    @GeneratedValue
    private Integer categoryId;
//    类目名字
    private String categoryName;
//    类目编码
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

    public ProductCategory() {
    }

    //方便直接创建带参数的 实例
    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
