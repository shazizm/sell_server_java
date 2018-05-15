package com.imooc.sell.controller;


import com.imooc.sell.config.WechatMpConfig;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;

//@RestController //RestController 是用来返回 json的
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    //1。配置
    @Autowired
    private WxMpService wxMpService; //TODO 这里生成的 wxMpService 已经是配好的了。在/config/WechatMpConfig里调 WechatAccountConfig（它里面调的application.yml）

    //2. 调用方法
    //要求授权用户信息 带上跳转url 向微信 要 code
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {

        String url = "http://mishi.fantreal.com/sell/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl)); //TODO
        log.info("【微信网页授权】获取code，result={}", redirectUrl);

        return "redirect:" + redirectUrl;
    }

    //跳转回服务器，服务器可以拿到 code 和 state，再用 code 拿 accessToken
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();

        //拿openid
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信网页授权】");
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg())
        }
        //如果没问题，就可以用getOpenId()来获取openid
        String openId = wxMpOAuth2AccessToken.getOpenId();

        return "redirect: " + returnUrl + "?openid=" + openId; //这里的 returnUrl 一般就是你要提供服务的网址，后面带上openid就是为了用户识别登陆用。
    }
}
