package com.imooc.sell.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WechatMpConfig { // 整个得看 sdk 说明估计才好明白

    @Autowired
    private WechatAccountConfig accountConfig;

    @Bean //不明白 Bean
    public WxMpService wxMpService(){
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage()); //调下面的配置
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){ //找一个接口的实现类，快捷键是？
        WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpConfigStorage.setAppId(accountConfig.getMpAppId()); //你能看懂的就是这个了
        wxMpConfigStorage.setSecret(accountConfig.getMpAppSecret()); //和这个，配置文件从 application.yml 里拿
        return wxMpConfigStorage;
    }
}
