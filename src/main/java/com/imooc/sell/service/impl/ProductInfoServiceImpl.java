package com.imooc.sell.service.impl;

import com.imooc.sell.dataObject.ProductInfo;
import com.imooc.sell.dto.CartDTO;
import com.imooc.sell.enums.ProductStatusEnum;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.repository.ProductInfoRepository;
import com.imooc.sell.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {


    @Autowired
    private ProductInfoRepository repository;

    @Override
    public ProductInfo findOne(String productId){
        return repository.findById(productId).get();

    }
    @Override
    public List<ProductInfo> findUpAll(){
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for(CartDTO cartDTO : cartDTOList){
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).get(); //这里如果找不到productid对应的商品，在.get()就抛出错误了，所以是一个系统错误，并没有被我们的ResultEnum 替换。
            if(productInfo == null) {//如果商品不存在
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(result);

            repository.save(productInfo);
        }
    }

    @Override
    @Transactional //这里是一个主订单的所有子商品项目，所以一个如果减库存失败，就要全部回滚。//待优化，这里需要Redis的锁机制。
    public void decreateStock(List<CartDTO> cartDTOList) {
        for(CartDTO cartDTO : cartDTOList){
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).get();
            if(productInfo == null) {//如果商品不存在
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if(result < 0 ){//如果用户要的商品量 减去 商品库存小于 0，说明库存不够
                throw new SellException(ResultEnum.PRODUCT_STOCK_NOT_ENOUGH);
            }

            productInfo.setProductStock(result);

            repository.save(productInfo);
        }
    }

    @Override
    public ProductInfo onSale(String productId) {
        Optional<ProductInfo> optional = repository.findById(productId);
        if(!optional.isPresent()){ //TODO 判断方法很奇葩
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);

        }
        ProductInfo productInfo = optional.get();
        if(productInfo.getProductStatusEnum() == ProductStatusEnum.UP){
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //更新 为上架
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return repository.save(productInfo);

    }

    @Override
    public ProductInfo offSale(String productId) {
        Optional<ProductInfo> optional = repository.findById(productId);
        if(!optional.isPresent()){ //TODO 判断方法很奇葩
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);

        }
        ProductInfo productInfo = optional.get();
        if(productInfo.getProductStatusEnum() == ProductStatusEnum.DOWN){
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //更新 为下架
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return repository.save(productInfo);
    }
}
