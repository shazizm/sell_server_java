package com.imooc.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//这玩意和 WechatAccountConfig 用法一样
@Data
@ConfigurationProperties(prefix = "project-url")
@Component
public class ProjectUrlConfig {

    //微信服务号授权url
    public String wechatMpAuthorize;

    //微信 开放平台 Web应用 授权url
    public String wechatOpenWebAuthorize;

    //系统配置连接
    public String sell;
}
