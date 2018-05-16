package com.imooc.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {
    //服务号 AppId
    private String mpAppId;
    //服务号 AppSecret
    private String mpAppSecret;
    //商户号
    private String mchId;
    //商户密钥
    private String mchKey;
    //商户证书路径 老师放在 /var/weixin_cert/h5.p12 ,需要java对目录有访问权限
    private String mchkeyPath;
    //微信支付异步通知地址
    private String notifyUrl;

}
