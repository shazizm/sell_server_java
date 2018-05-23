package com.imooc.sell.service;

import com.imooc.sell.dataObject.SellerInfo;

public interface SellerService {

    SellerInfo findSellerInfoByOpenid(String openid);
}
