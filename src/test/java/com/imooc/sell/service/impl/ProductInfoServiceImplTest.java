package com.imooc.sell.service.impl;

import com.imooc.sell.dataObject.ProductInfo;
import com.imooc.sell.enums.ProductStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceImplTest {
    @Autowired
    private ProductInfoServiceImpl productInfoService;

    @Test
    public void findOne() {
        ProductInfo productInfo = productInfoService.findOne("suiji123456");
        Assert.assertEquals("suiji123456", productInfo.getProductId());
    }

    @Test
    public void findUpAll() {
        List<ProductInfo> productInfoList = productInfoService.findUpAll();
        Assert.assertNotEquals(0,productInfoList.size());
    }

    @Test
    public void findAll() {

        //这里 new PageRequest 不推荐用了, PageRequest.fo()
        PageRequest request = PageRequest.of(0,2);
        Page<ProductInfo> productInfoPage = productInfoService.findAll(request);
//        System.out.println(productInfoPage.getTotalElements());
        Assert.assertNotEquals(0, productInfoPage.getTotalElements());
    }

    @Test
    public void save() {
        ProductInfo productInfo = new ProductInfo();

        productInfo.setProductId("suiji000002");
        productInfo.setProductName("环保袋");
        productInfo.setProductPrice(new BigDecimal(0.01));
        productInfo.setProductStock(1000);
        productInfo.setProductDescription("反正就是测试用");
        productInfo.setProductIcon("http://vvv.jpg");
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode()); //上架
        productInfo.setCategoryType(2);

        ProductInfo result = productInfoService.save(productInfo);

        Assert.assertNotNull(result);
    }
}
