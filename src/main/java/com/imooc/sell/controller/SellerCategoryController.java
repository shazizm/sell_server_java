package com.imooc.sell.controller;

import com.imooc.sell.dataObject.ProductCategory;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.form.CategoryForm;
import com.imooc.sell.service.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/category")
@Slf4j
public class SellerCategoryController {

    @Autowired
    private ProductCategoryService categoryService;

    //类目列表
    @GetMapping("/list")
    public ModelAndView list(Map<String, Object> map){
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList", categoryList);
        return new ModelAndView("category/list", map);
    }

    //展示
    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                              Map<String, Object> map){
        if(categoryId != null ) { //判断空
            ProductCategory productCategory = categoryService.findOne(categoryId);
            map.put("productCategory", productCategory);
        }

        return new ModelAndView("category/index", map);
    }

    //新建 和 修改
    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm form,
                             BindingResult bindingResult,
                             Map<String, Object> map){
        if(bindingResult.hasErrors()){
            log.error("【卖家端类目新建/更新】 发生异常 {}", bindingResult.getFieldError().getDefaultMessage());
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/category/index");
            return new ModelAndView("common/error", map);
        }

        ProductCategory productCategory = new ProductCategory();
        try {
            if (form.getCategoryId() != null) {
                productCategory = categoryService.findOne(form.getCategoryId());
            }
            BeanUtils.copyProperties(form, productCategory);

            categoryService.save(productCategory);
        } catch(SellException e){
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/category/index");
            return new ModelAndView("common/error", map);
        }

        //如果一切正常
        map.put("url", "/sell/seller/category/list");
        return new ModelAndView("common/success", map);

    }
}
