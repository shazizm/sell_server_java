package com.imooc.sell.config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component //
public class WechatPayConfig {

    @Autowired
    private WechatAccountConfig accountConfig; //引入config文件

    @Bean
    public BestPayServiceImpl bestPayService(){


        BestPayServiceImpl bestPayService = new BestPayServiceImpl();

        //传入 支付配置
        bestPayService.setWxPayH5Config(wxPayH5Config());
        return bestPayService;
    }

    @Bean
    public WxPayH5Config wxPayH5Config(){
        WxPayH5Config wxPayH5Config = new WxPayH5Config();

        //设置 支付配置 需要的 参数
        wxPayH5Config.setAppId(accountConfig.getMpAppId());
        wxPayH5Config.setAppSecret(accountConfig.getMpAppSecret());
        wxPayH5Config.setMchId(accountConfig.getMchId());
        wxPayH5Config.setMchKey(accountConfig.getMchId());
        wxPayH5Config.setKeyPath(accountConfig.getMchkeyPath());
        wxPayH5Config.setNotifyUrl(accountConfig.getNotifyUrl());

        return wxPayH5Config;

    }
}
