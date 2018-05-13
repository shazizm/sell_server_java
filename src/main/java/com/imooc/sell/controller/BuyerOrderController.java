package com.imooc.sell.controller;

import com.imooc.sell.converter.OrderForm2OrderDTOConverter;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.form.OrderForm;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.utils.ResultVOUtil;
import com.imooc.sell.viewObject.ResultViewObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderService orderService;

    //创建订单
    @PostMapping("/create")
    public ResultViewObject<Map<String, String>> create(@Valid OrderForm orderForm, //函数 入参 验证 valid
                                                        BindingResult bindingResult) { // 入参 验证 结果 bindingResult
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】表单校验失败，orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        //转换完，先判断一下orderDTO是不是空的
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO createResult = orderService.create(orderDTO);

        //因为 本 函数定义 返回结果类型是 ResultViewObject< Map<String, String> >
        Map<String, String> map = new HashMap<>();
        map.put("orderId", createResult.getOrderId()); //这里是依据前端API接口定义的 只返回一个 orderId

        return ResultVOUtil.success(map); //返回的是一个 ResultVO，最后套上定义的标准 ResultVO的格式。这里map是一个 Object对象
    }

    //订单列表
    @GetMapping("/list")
    public ResultViewObject<List<OrderDTO>> list(@RequestParam("openid") String openid, //与上面create区别，这里老师直接写了，没有搞一个 orderForm的东西
                                                 @RequestParam(value = "page", defaultValue = "0") Integer page, //这里不传的话，会有一个默认值
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("【查询订单列表】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        PageRequest request = PageRequest.of(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, request);

        return ResultVOUtil.success(orderDTOPage.getContent());
    }

    //订单详情
    @GetMapping("/detail")
    public ResultViewObject<OrderDTO> detail(@RequestParam("openid") String openid,
                                             @RequestParam("orderId") String orderId) {
        //TODO 不安全做法，改进
        OrderDTO orderDTO = orderService.findOne(orderId);
        return ResultVOUtil.success(orderDTO);
    }

    //取消订单
    @PostMapping("/cancel")
    public ResultViewObject cancel(@RequestParam("openid") String openid,
                                   @RequestParam("orderId") String orderId) {
        //TODO 不安全做法，改进
        OrderDTO orderDTO = orderService.findOne(orderId);

        orderService.cancel(orderDTO);

        return ResultVOUtil.success();
    }
}