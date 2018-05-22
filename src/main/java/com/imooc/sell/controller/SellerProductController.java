package com.imooc.sell.controller;

import com.imooc.sell.dataObject.ProductCategory;
import com.imooc.sell.dataObject.ProductInfo;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.form.ProductForm;
import com.imooc.sell.service.ProductCategoryService;
import com.imooc.sell.service.ProductInfoService;
import com.imooc.sell.utils.GenKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sun.security.util.KeyUtil;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/product")
@Slf4j
public class SellerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,        //第几页
                             @RequestParam(value = "size", defaultValue = "10") Integer size,       //一页有多少条数据
                             Map<String, Object> map){

        PageRequest request = PageRequest.of(page-1, size); //因为是从0开始,不过这里得试试
        Page<ProductInfo> productInfoPage = productInfoService.findAll(request);

        //orderDTOPage.getTotalPages();

        map.put("productInfoPage", productInfoPage);
        map.put("currentPage", page);
        map.put("size", size);
        return new ModelAndView("product/list", map);
    }

    @RequestMapping("/on_sale")
    public ModelAndView onSale(@RequestParam("productId") String productId,
                               Map<String, Object> map){

        try {
            productInfoService.onSale(productId);
        } catch (SellException e){

            log.error("【卖家端上架商品时】 发生异常 {}", e.getMessage());
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }

    @RequestMapping("/off_sale")
    public ModelAndView offSale(@RequestParam("productId") String productId,
                               Map<String, Object> map){

        try {
            productInfoService.offSale(productId);
        } catch (SellException e){

            log.error("【卖家端下架商品时】 发生异常 {}", e.getMessage());
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }

    //查看 商品
    @RequestMapping("/index")
    public ModelAndView index(@RequestParam(value = "productId", required = false) String productId,
                      Map<String, Object> map){
        if(!StringUtils.isEmpty(productId)){
            ProductInfo productInfo = productInfoService.findOne(productId);
            map.put("productInfo", productInfo);
        }

        //查询所有商品类目
        List<ProductCategory> productCategoryList = productCategoryService.findAll();
        map.put("categoryList", productCategoryList);

        return new ModelAndView("product/index", map);
    }

    //创建 和 更新 都用这个方法
    @PostMapping("/save")
    public ModelAndView save(@Valid ProductForm form,
                             BindingResult bindingResult, //这里验证规则是怎么来？？
                             Map<String, Object> map){
        if(bindingResult.hasErrors()){ //先判断有没有表单校验错误，如果有错误先跳到错误界面
            log.error("【卖家端商品新建/更新】 发生异常 {}", bindingResult.getFieldError().getDefaultMessage());
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("common/error", map);
        }

        ProductInfo productInfo = new ProductInfo(); // new的时候自带上架默认值
        try{
            if(!StringUtils.isEmpty(form.getProductId())){ //有productId 这是修改操作
                productInfo = productInfoService.findOne(form.getProductId());
            }else{  // 没有 productId 这是 新增操作
                //新增需要  随机一个 productId
                form.setProductId(GenKeyUtil.genUniqueKey());
            }

            BeanUtils.copyProperties(form, productInfo); //TODO 前面的覆盖后面的复制方式，如果key没有就不覆盖
            productInfoService.save(productInfo);
        }catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/product/index");
            return new ModelAndView("common/error", map);
        }


        //如果一切正常
        map.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }
}
