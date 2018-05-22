<html>
    <#include "../common/header.ftl">
<body>

<div id="wrapper" class="toggled">
<#--边栏 sidebar-->
            <#include "../common/nav.ftl">
<#--主要内容 content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                    <div class="col-md-12 column">
                        <form role="form" method="post" action="/sell/seller/product/save">
                            <div class="form-group">
                                <label>名称</label>
                                <input name="productName" value="${(productInfo.productName)!''}" type="text" class="form-control" />
                            </div>
                            <div class="form-group">
                                <label>价格</label>
                                <input name="productPrice" value="${(productInfo.productPrice)!''}" type="text" class="form-control" />
                            </div>
                            <div class="form-group">
                                <label>库存</label>
                                <input name="productStock" value="${(productInfo.productStock)!''}" type="text" class="form-control" />
                            </div>
                            <div class="form-group">
                                <label>描述</label>
                                <input name="productDescription" value="${(productInfo.productDescription)!''}" type="number" class="form-control" />
                            </div>

                            <div class="form-group">
                                <label>图片</label>
                                <img height="100" width="100" src="${(productInfo.productIcon)!''}" alt="">
                                <input name="productIcon" value="${(productInfo.productIcon)!''}" type="number" class="form-control"/>
                            </div>

                            <div class="form-group">
                                <label>类目</label>
                                <select name="categoryType" class="form-control">
                                    <#list categoryList as item>
                                        <#--TODO 值得注意的freemaker语法-->
                                        <option value="${item.categoryType}"
                                            <#if (productInfo.categoryType)?? && productInfo.categoryType == item.categoryType>
                                                selected
                                            </#if>
                                        >
                                            ${item.categoryName}
                                        </option>
                                    </#list>
                                </select>
                            </div>
                            <#--TODO 这里使用了隐藏参数-->
                            <input hidden type="text" name="productId" value="${(productInfo.productId)!''}">
                            <button type="submit" class="btn btn-default">提交</button>
                        </form>
                    </div>
                </div>
        </div>
    </div>
</div>

</body>
</html>