package com.imooc.sell.service.impl;

import com.imooc.sell.config.WechatAccountConfig;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.service.PushMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class PushMessageServiceImpl implements PushMessageService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WechatAccountConfig accountConfig;

    @Override
    public void orderStatus(OrderDTO orderDTO) {

        //1.老师第一个sdk 生成一个template并配置好
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setTemplateId(accountConfig.getTemplateId().get("orderStatus")); // 对应配置写在/config/WechatAccountConfig文件里。这里得在公众号 申请开通，开通后，我添加的是"订单状态更新通知"
        templateMessage.setToUser(orderDTO.getBuyerOpenid()); //拿订单人的公众号的openid

        //2.造个数据
        List<WxMpTemplateData> data = Arrays.asList( //按着公众号 ，的模板消息，的 "订单状态更新通知"来
                new WxMpTemplateData("first","淫娃，请记得收货"),
                new WxMpTemplateData("keyword1", "微信点餐"),
                new WxMpTemplateData("keyword2", "13381193434"),
                new WxMpTemplateData("keyword3", orderDTO.getOrderId()),
                new WxMpTemplateData("keyword4", "￥" + orderDTO.getOrderStatusEnum().getMessage()),
                new WxMpTemplateData("keyword5", "欢迎再次光临")
        );

        //3.装进tempalate
        templateMessage.setData(data);

        try{

            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        }catch(WxErrorException e){
            log.error("【微信模板消息】发送失败，{}", e);
        }
    }
}
