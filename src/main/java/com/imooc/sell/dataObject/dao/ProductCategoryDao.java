package com.imooc.sell.dataObject.dao;

import com.imooc.sell.dataObject.mapper.ProductCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ProductCategoryDao {

    @Autowired
    ProductCategoryMapper mapper;

    public int insertByMap(Map<String, Object> map){
        return mapper.insertByMap(map);
    }
}
