package com.imooc.sell.controller;


import com.imooc.sell.config.ProjectUrlConfig;
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

import java.net.URLEncoder;

//@RestController //RestController 是用来返回 json的
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    //ProjectUrl 配置
    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //12-5 一。配置 web端扫码登陆
    @Autowired
    private WxMpService wxOpenWebService; //TODO 同下面那个 TODO 的注释。

    //1。配置
    @Autowired
    private WxMpService wxMpService; //TODO 这里生成的 wxMpService 已经是配好的了。在/config/WechatMpConfig里调 WechatAccountConfig（它里面调的application.yml）

    //2. 调用方法
    //要求授权用户信息 带上跳转url 向微信 要 code,测试时把链接下面链接发给自己微信，点击即可
    //http://mishi.fantreal.com/sell/wechat/authorize?state=http://mishi.fantreal.com/sell/buyer/product/list
    @GetMapping("/authorize")
    public String authorize(@RequestParam("state") String returnUrl) {

        String url = projectUrlConfig.wechatMpAuthorize + "/sell/wechat/userInfo"; //这里先写死之后，调通后，可以做一个动态配置
        //第一个参数是 redirectUrl，第二个参数是 scope，第三个参数是 state
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl)); //TODO
        log.info("【微信公众号网页授权】获取code，result={}", redirectUrl);

        return "redirect:" + redirectUrl;
    }

    //跳转回服务器，服务器可以拿到 code 和 state，再用 code 拿 accessToken
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        log.info("【微信公众号网页授权】跳转完毕，获取code，result={}", code);
        log.info("【微信公众号网页授权】跳转完毕，获取returnUrl，result={}", returnUrl);
        //拿openid
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信公众号网页授权】");
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        //如果没问题，就可以用getOpenId()来获取openid
        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("【微信公众号网页授权】跳转完毕，sdk拿openid，，result={}", openId);
        return "redirect:" + returnUrl + "?openid=" + openId; //这里的 returnUrl 一般就是你要提供服务的网址，后面带上openid就是为了用户识别登陆用。
    }



    //测试用链接： http://mishi.fantreal.com/sell/wechat/qrAuthorize?state=http://mishi.fantreal.com/sell/seller/login
    //12-5 二。调用方法 ，这里其实和 上面两个差不多。因为要扫二维码，我们就起名qr开头来 区分吧。
    @GetMapping("/qrAuthorize")
    public String qrAuthorize(@RequestParam("state") String returnUrl){
        String url= projectUrlConfig.wechatOpenWebAuthorize + "/sell/wechat/qrUserInfo"; //先写死，也不知道什么时候能写活
        //第一个参数是 redirectUrl，第二个参数是 scope(网页这里文档要求是 wx.login)，第三个参数是 state
        String redirectUrl = wxOpenWebService.buildQrConnectUrl(url, WxConsts.QrConnectScope.SNSAPI_LOGIN,URLEncoder.encode(returnUrl));
        return "redirect:" + redirectUrl;
    }

    //12-5
    @GetMapping("/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code,
                             @RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        log.info("【web网页微信扫码授权】跳转完毕，获取code，result={}", code);
        log.info("【web网页微信扫码授权】跳转完毕，获取returnUrl，result={}", returnUrl);
        //拿openid
        try {
            wxMpOAuth2AccessToken = wxOpenWebService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【web网页微信扫码授权】");
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        //如果没问题，就可以用getOpenId()来获取openid //张铭在幻想现实web应用的 openid = oJIwWwJV4WUD5pomonrFhvT6naMI
        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("【web网页微信扫码授权】跳转完毕，sdk拿openid，，result={}", openId);
        return "redirect:" + returnUrl + "?openid=" + openId; //这里的 returnUrl 一般就是你要提供服务的网址，后面带上openid就是为了用户识别登陆用。
    }



}
