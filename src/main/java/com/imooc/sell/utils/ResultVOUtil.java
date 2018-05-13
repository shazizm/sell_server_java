package com.imooc.sell.utils;

import com.imooc.sell.viewObject.ResultViewObject;

public class ResultVOUtil {

    public static ResultViewObject success(Object obj){
        ResultViewObject resultVO = new ResultViewObject();
        resultVO.setData(obj);
        resultVO.setCode(0);
        resultVO.setMsg("请求成功");

        return resultVO;
    }

    public static ResultViewObject success(){
        return success(null);
    }

    public static ResultViewObject error(Integer code, String msg){
        ResultViewObject resultVO = new ResultViewObject();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
