package com.imooc.sell.controller;


//卖家端订单

import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.service.OrderService;
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
@RequestMapping("/seller/order")
public class SellerOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,        //第几页
                             @RequestParam(value = "size", defaultValue = "10") Integer size,       //一页有多少条数据
                             Map<String, Object> map){ // model and view 用的入参

        PageRequest request = PageRequest.of(page-1, size); //因为是从0开始,不过这里得试试
        Page<OrderDTO> orderDTOPage = orderService.findList(request);

        map.put("orderDTOPage", orderDTOPage);
        return new ModelAndView("order/list", map);

    }
}
