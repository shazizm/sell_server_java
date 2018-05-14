package com.imooc.sell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


//这个文件是 手动 如何 获取授权，但是老师提倡用 sdk 所以这个文件不会在项目里用

@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    //上一步要访问 https://open.weixin.qq.com/connect/oauth2/authorize?
    // appid=wx2f8a32822d551206
    //&redirect_uri=http://mishi.fantreal.com/sell/weixin/auth
    //&scope=snsapi_base
    //&state=storeid123

    //https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2f8a32822d551206&redirect_uri=http://mishi.fantreal.com/weixin/auth&scope=snsapi_base&state=storeid123
    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code,
                     @RequestParam("state") String state){
        log.info("我是 公众号授权登陆 回调url ，这里能获取 code 和 state 的值");
        log.info("code={}, state={}", code,state);

        //拿到code后访问
        //https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx2f8a32822d551206&secret=b75b7137c3b0345c8ac5318d9708d132&code=xxx&grant_type=authorization_code
        String url = "";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("response={}", response);

        //response 里有 access_token, expires_in refresh_token openid scope="snsapi_base"
        //主要拿到 openid


        //如果要拉取userInfo，scope一开始得是 snsapi_info
        //https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
        //这样能拿到unionid，用户名，头像，城市 等信息
    }


}
