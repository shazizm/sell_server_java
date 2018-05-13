package com.imooc.sell;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
// 这里可以通过 @Slf4j 来简化 logger，直接log就行了。（貌似@Slf4j 和 @Data 是用的同一个库） 依赖包lombok
@Slf4j
public class LoggerTest {

//    private final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void test1(){
        String name = "imooc";
        String password = "123456";
        log.debug("debug...");
        log.info("info...");
        log.info("name: " + name + "password: " + password);
        log.info("name: {}, password: {}", name , password);  //上一行的另一种实现
        log.error("erro...");
    }
}
