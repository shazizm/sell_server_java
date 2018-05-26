package com.imooc.sell.dataObject.mapper;

import com.imooc.sell.dataObject.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductCategoryMapper {

    //第一种 插入方式，Map
    // 插入两个值 category_name 和 category_type 需要和数据库的字段保持一致 。values 后面是输入的值 #{category_name 就从Map里来取。
    @Insert("insert into product_category(category_name, category_type) values(#{categoryName, jdbcType=VARCHAR}, #{categoryType, jdbcType=INTEGER})")
    int insertByMap(Map<String, Object> map);

    //第二种 插入方式，Object，一般更多用这个
    @Insert("insert into product_category(category_name, category_type) values(#{categoryName, jdbcType=VARCHAR}, #{categoryType, jdbcType=INTEGER})")
    int insertByObject(ProductCategory productCategory);

    //查找方式 按type查，只有一条
    @Select("select * from product_category where category_type =#{categoryType}")
    @Results(
            {       //下面三行为了 字段映射用。只有这里设置了，才会查出来，比如createTime没写就不会查出来
                    @Result(column = "category_name", property = "categoryName"),
                    @Result(column = "category_type", property = "categoryType"),
                    @Result(column = "category_id", property = "categoryId"),
            }
    )
    ProductCategory findByCategoryType(Integer categoryType);

    //查找方式 按name查，就有可能是多条了
    @Select("select * from product_category where category_name =#{categoryName}")
    @Results(
            {       //下面三行为了 字段映射用。只有这里设置了，才会查出来，比如createTime没写就不会查出来
                    @Result(column = "category_name", property = "categoryName"),
                    @Result(column = "category_type", property = "categoryType"),
                    @Result(column = "category_id", property = "categoryId"),
            }
    )
    List<ProductCategory> findByCategoryName(String categoryName);

    //根据某一字段type 更新 name。 官方要求多个参数的时候要用 @Param
    @Update("update product_category set category_name = #{categoryName} where category_type = #{categoryType}")
    int updateByCategoryType(@Param("categoryName") String categoryName,
                             @Param("categoryType") Integer categoryType);

    //根据一个对象 更新, 使用可以对应 test 来看
    @Update("update product_category set category_name = #{categoryName} where category_type = #{categoryType}")
    int updateByObject(ProductCategory productCategory);

    //删除
    @Delete("delete from product_category where category_type = #{categoryType}")
    int deleteByCategoryType(Integer categoryType);

    //把SQL语句放在xml文件里，调用的方式 //xml 放在/resources/mapper下
    ProductCategory selectByCategoryType(Integer categoryType);

}
