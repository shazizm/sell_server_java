package com.imooc.sell.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component //TODO 不懂
public class WechatOpenWebConfig {

    @Autowired
    private WechatAccountConfig accountConfig;

    @Bean //这个用的老师推荐的第一个sdk WxMpService，虽然用的Mp，但是支持open的扫码登陆，这个Bean搞好，就可以去写controller了
    public WxMpService wxOpenWebService(){
        WxMpService wxOpenWebService = new WxMpServiceImpl(); //TODO 不懂 参考 WechatMpConfig
        wxOpenWebService.setWxMpConfigStorage(wxMpConfigStorage()); //这里把 下面那个 Bean 放进来，就配置好了
        return wxOpenWebService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setAppId(accountConfig.getOpenWebAppId());
        wxMpInMemoryConfigStorage.setSecret(accountConfig.getOpenWebAppSecret());
        return wxMpInMemoryConfigStorage;
    }
}
