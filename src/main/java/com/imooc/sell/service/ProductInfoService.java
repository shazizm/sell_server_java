package com.imooc.sell.service;

import com.imooc.sell.dataObject.ProductInfo;
import com.imooc.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductInfoService {
    ProductInfo findOne(String productId);

    //    查询所有在架的商品列表
    List<ProductInfo> findUpAll();
//    配置分页
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

//    加库存
    void increaseStock(List<CartDTO> cartDTOList);

//    减库存
    void decreateStock(List<CartDTO> cartDTOList);
}
