package com.imooc.sell.viewObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


//商品（包含类目）
@Data
public class ProductVO {

    @JsonProperty("name")  // 把categoryName映射成json接口格式上的name
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;




}
