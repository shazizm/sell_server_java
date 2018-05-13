package com.imooc.sell.repository;

import com.imooc.sell.dataObject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository repository;

    private final String OPENID = "openid1234";

    @Test
    public  void saveTest(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("suiji0123457");
        orderMaster.setBuyerName("孙巍");
        orderMaster.setBuyerPhone("13366668888");
        orderMaster.setBuyerAddress("情趣密室");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setOrderAmount(new BigDecimal(25.5));

        OrderMaster result = repository.save(orderMaster);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByBuyerOpenid() {
        PageRequest request = PageRequest.of(0,3); // 从第0页开始 ，这一页最多总共1项

        Page<OrderMaster> result = repository.findByBuyerOpenid(OPENID, request);
        Assert.assertNotEquals(0,result.getTotalElements());
        System.out.println(result.getTotalElements());

    }
}