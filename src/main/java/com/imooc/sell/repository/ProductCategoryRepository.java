package com.imooc.sell.repository;

import com.imooc.sell.dataObject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//这里的 Repository 其实就是 DAO Data-Access-Object,不用写一句 SQL 语句，就可以用了
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    //下面的 findBy 后面跟的 名称 要按规范写
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
