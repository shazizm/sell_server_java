package com.imooc.sell.service.impl;

import com.imooc.sell.converter.OrderMaster2OrderDTOConverter;
import com.imooc.sell.dataObject.OrderDetail;
import com.imooc.sell.dataObject.OrderMaster;
import com.imooc.sell.dataObject.ProductInfo;
import com.imooc.sell.dto.CartDTO;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.repository.OrderDetailRepository;
import com.imooc.sell.repository.OrderMasterRepository;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.service.PayService;
import com.imooc.sell.service.ProductInfoService;
import com.imooc.sell.utils.GenKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.CollectionUtils;
import sun.security.util.KeyUtil;

import java.awt.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private PayService payService;


    @Override
    @Transactional //这样如果扣库存失败的话，就会全部回轮。
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId = GenKeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(0);

//对应下面a        List<CartDTO> cartDTOList = new ArrayList<>();

        //1. 查询商品 （数量，价格）
        for (OrderDetail item: orderDTO.getOrderDetailList()){ //循环每一个商品，生成对应orderId的orderDetail 内容
            ProductInfo productInfo = productInfoService.findOne(item.getProductId());
            if(productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //如果 有商品，不在这里检查是否有库存，在第4步检查

            //2, 计算订单总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(item.getProductQuantity()))
                    .add(orderAmount);
            //订单详情入库

            BeanUtils.copyProperties(productInfo,item); //然后把其它 商品属性 copy 到要创建的订单详情item里。
            item.setDetailId(GenKeyUtil.genUniqueKey());
            item.setOrderId(orderId); //这个订单的id 在创建时就要生成

            orderDetailRepository.save(item); // 订单详情，写数据库。总订单里的 每一个商品订单 都save

//对应上面a            CartDTO cartDTO = new CartDTO(item.getProductId(),item.getProductQuantity());
//对应上面a           cartDTOList.add(cartDTO);

        }

        //3，写入订单数据库（orderMaster 和 orderDetail)
        OrderMaster newOrderMaster = new OrderMaster();

        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, newOrderMaster); //前面的值，拷贝到后面，覆盖，没有的key就增加。
        newOrderMaster.setOrderAmount(orderAmount); //奇怪 这里的值并没有附上
        newOrderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode()); //奇怪 这里的值并没有附上
        newOrderMaster.setPayStatus(PayStatusEnum.WAIT.getCode()); //奇怪 这里的值并没有附上
        orderMasterRepository.save(newOrderMaster); //主订单，写数据库


        //4，成功的话扣库存
        // 替换上面注释的实现方式a         //lambda的新式写法  //生成的是 库存用的 入参
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
            new CartDTO(e.getProductId(),e.getProductQuantity())
        ).collect(Collectors.toList());

        productInfoService.decreateStock(cartDTOList);
        return orderDTO; //为啥返回是它？
    }

    @Override
    public OrderDTO findOne(String orderId) {

        OrderMaster orderMaster = orderMasterRepository.findById(orderId).get();
        if(orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //如果有 继续查询 订单详情
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        // 判断List为空 用一个奇怪的方法
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        OrderDTO newOrderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, newOrderDTO); //把查出的orderMaster 加到空newOrderDTO

        newOrderDTO.setOrderDetailList(orderDetailList); //在把查出来的 orderDetailList 加到空newOrderDTO上

        return newOrderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);

        List<OrderDTO> orderDTOList =OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent()); //补学 这里入参应该是一个OrderMaster的List

        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements()); //补学 PageImpl
        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {

        OrderMaster orderMaster = new OrderMaster();
        //业务说明 一般取消什么东西，都得先判断一下状态，只有有些状态才可以取消。比如已经被卖家端 接单，那就不能再取消订单了

        //1 判断订单状态 (已取消 和 完结 状态 就不能再取消了，只能是 新订单)
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){ //如果 订单状态 不等于 0新建订单，那就报错
            log.error("【取消订单】订单状态不正确，orderId={}, orderStatus={}", orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //2 修改订单状态，修改为 取消
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster); // 把前一个 copy 给 后一个
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null){
            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }
        //3 返还库存
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){ //先判断一下订单里有没有商品 //这里也用到了 List 判断空的办法
            log.error("【取消订单】订单中无商品详情，orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }

        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productInfoService.increaseStock(cartDTOList); //这里入参是一个 CartDTO，在上三行拼一个出来

        //4 如果已经支付，需要退款
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            //TODO 8-7 11:30
            payService.refund(orderDTO);
            //退款成功后，在微信端，可以看到退款通知，白天一般立刻到。
            //从系统看，refundResponse.outRefundNo 有 值，就是退款成功。
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //1，判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【完结订单】订单状态不正确，orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //2，修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());

        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);

        OrderMaster updateResult = orderMasterRepository.save(orderMaster);

        if(updateResult == null){
            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) { //支付完结才调用

        //1 判断订单状态 (已取消 和 完结 状态 就不能再取消了，只能是 新订单)
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){ //如果 订单状态 不等于 0新建订单，那就报错
            log.error("【订单支付完成】订单状态不正确,orderId={}, orderStatus={}", orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //2 判断支付状态
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("【订单支付完成】订单支付状态不正确,orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //3 修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());

        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);

        OrderMaster updateResult = orderMasterRepository.save(orderMaster);

        if(updateResult == null){
            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }
        // 支付状态是支付状态，不修改订单状态
        return orderDTO;
    }
}
