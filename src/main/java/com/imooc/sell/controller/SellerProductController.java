package com.imooc.sell.controller;

import com.imooc.sell.dataObject.ProductInfo;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,        //第几页
                             @RequestParam(value = "size", defaultValue = "10") Integer size,       //一页有多少条数据
                             Map<String, Object> map){

        PageRequest request = PageRequest.of(page-1, size); //因为是从0开始,不过这里得试试
        Page<ProductInfo> productInfoPage = productInfoService.findAll(request);

        //orderDTOPage.getTotalPages();

        map.put("productInfoPage", productInfoPage);
        map.put("currentPage", page);
        map.put("size", size);
        return new ModelAndView("product/list", map);
    }
}
