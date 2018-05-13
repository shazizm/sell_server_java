package com.imooc.sell.controller;

import com.imooc.sell.dataObject.ProductCategory;
import com.imooc.sell.dataObject.ProductInfo;
import com.imooc.sell.service.ProductCategoryService;
import com.imooc.sell.service.ProductInfoService;
import com.imooc.sell.utils.ResultVOUtil;
import com.imooc.sell.viewObject.ProductInfoVO;
import com.imooc.sell.viewObject.ProductVO;
import com.imooc.sell.viewObject.ResultViewObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController //返回的是json格式，所以这里用RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list")
    public ResultViewObject list(){
        //1. 查询所有的上架商品
        List<ProductInfo> productInfoList = productInfoService.findUpAll();


        //2. 查询类目（和上面的查询商品要一次性查询，提升性能）
        //2.1传统 拿 query list
//        List<Integer> queryCategoryTypeList = new ArrayList<>();
//        for(ProductInfo items : productInfoList){
//            queryCategoryTypeList.add(items.getCategoryType());
//        }
        //2.2lambda 拿 query list（java8，lambda）
        List<Integer> queryCategoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());


        List<ProductCategory> productCategoryList = productCategoryService.findByCategoryTypeIn(queryCategoryTypeList);
        //3. 数据拼装
        List<ProductVO> productVOList = new ArrayList<>(); //最终返回json的data部分
        for(ProductCategory item: productCategoryList){
            ProductVO productVO = new ProductVO();
            // 1
            productVO.setCategoryName(item.getCategoryName());
            // 2
            productVO.setCategoryType(item.getCategoryType());


            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for(ProductInfo itemitem: productInfoList){
                if(itemitem.getCategoryType().equals(item.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    //这里用了一个简单的方法库 BeanUtils 把itemitem的值直接copy给 productInfoVO
                    BeanUtils.copyProperties(itemitem,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            // 3
            productVO.setProductInfoVOList(productInfoVOList);

            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }
}
