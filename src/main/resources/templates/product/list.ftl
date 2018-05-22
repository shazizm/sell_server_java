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
                            <table class="table table-striped table-condensed">
                                <thead>
                                <tr>
                                    <th>产品id</th>
                                    <th>名称</th>
                                    <th>图片</th>
                                    <th>单价</th>
                                    <th>库存</th>
                                    <th>描述</th>
                                    <th>类目</th>
                                    <th>创建时间</th>
                                    <th>修改时间</th>
                                    <th colspan="2">操作</th>
                                </tr>
                                </thead>
                                <tbody>

                            <#list productInfoPage.content as item>

                            <tr>
                                <td>${item.productId}</td>
                                <td>${item.productName}</td>
                                <td><img height="100" width="100" src="${item.productIcon}" alt=""></td>
                                <td>${item.productPrice}</td>
                                <td>${item.productStock}</td>
                                <td>${item.productDescription}</td>
                                <td>${item.categoryType}</td>
                                <td>${item.createTime}</td>
                                <td>${item.updateTime}</td>
                                <td><a href="/sell/seller/product/index?productId=${item.productId}">修改</a></td>
                                <td>
                                    <#if item.getProductStatusEnum().message == "在架">
                                        <a href="/sell/seller/product/off_sale?productId=${item.productId}">下架</a>
                                    <#else>
                                        <a href="/sell/seller/product/on_sale?productId=${item.productId}">上架</a>
                                    </#if>
                                </td>
                            </tr>

                            </#list>

                                </tbody>
                            </table>
                        </div>

                    <#--分页-->
                        <div class="col-md-12 column">
                            <ul class="pagination pull-right">

                            <#if currentPage lte 1>
                                <li class="disabled"><a href="#">上一页</a></li>
                            <#else>
                                <li><a href="/sell/seller/product/list?page=${currentPage - 1}&size=${size}">上一页</a></li>
                            </#if>

                            <#-- 1 ..3 的意思就是 自动变成一个字符串-->
                            <#list 1..productInfoPage.getTotalPages() as index>
                                <#if currentPage == index>
                                    <li class="disabled"><a href="#">${index}</a></li>
                                <#else>
                                    <li><a href="/sell/seller/product/list?page=${index}&size=${size}">${index}</a></li>
                                </#if>
                            </#list>

                            <#if currentPage gte productInfoPage.getTotalPages()>
                                <li class="disabled"><a href="#">下一页</a></li>
                            <#else>
                                <li><a href="/sell/seller/product/list?page=${currentPage + 1}&size=${size}">下一页</a></li>
                            </#if>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>