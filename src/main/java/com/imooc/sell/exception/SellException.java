package com.imooc.sell.exception;

import com.imooc.sell.enums.ResultEnum;
import lombok.Getter;

@Getter
public class SellException extends RuntimeException {

    private Integer code;

    public  SellException(ResultEnum resultEnum){
        super(resultEnum.getMessage()); //传到 父类的构造方法里 message，当然在你知道父类里有 message

        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
