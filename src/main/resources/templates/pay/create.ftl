<script>
    //按理说这个代码要在前端代码里，比如vue里，但是如果有多个页面都需要支付，这样把代码统一到后端，vue端只掉一个orderId 和 returnUrl 就可以了
    //通过 create.frl 模板构建的
    function onBridgeReady(){
        WeixinJSBridge.invoke(
                'getBrandWCPayRequest', {
                    "appId":"${payResponse.appId}",     //公众号名称，由商户传入
                    "timeStamp":"${payResponse.timeStamp}",         //时间戳，自1970年以来的秒数
                    "nonceStr":"${payResponse.nonceStr}", //随机串
                    "package":"${payResponse.packAge}",
                    "signType":"${payResponse.signType}",         //微信签名方式：
                    "paySign":"${payResponse.paySign}" //微信签名
                    // 这上面这些对应的值，老师是从PayServiceImplTest.create跑测试，跑出来的。对应填好就行，package的值从wx开始复制
                },
                function(res){
                    //if(res.err_msg == "get_brand_wcpay_request:ok" ) {} //ok 支付成功, cancel 支付过程中用户取消, fail 支付失败
                    // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                    // 调用支付JSAPI 如果提示缺少参数：total_fee，请检查预支付会话标识prepay_id是否已失效

                    //这里不做状态判断，直接传到后端

                    location.href = "${returnUrl}";


                }
        );
    }
    if (typeof WeixinJSBridge == "undefined"){
        if( document.addEventListener ){
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        }else if (document.attachEvent){
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    }else{
        onBridgeReady();
    }
</script>