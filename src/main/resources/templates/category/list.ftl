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
                            <th>类目id</th>
                            <th>名称</th>
                            <th>type</th>
                            <th>创建时间</th>
                            <th>修改时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>

                            <#list categoryList as item>

                            <tr>
                                <td>${item.categoryId}</td>
                                <td>${item.categoryName}</td>
                                <td>${item.categoryType}</td>
                                <td>${item.createTime}</td>
                                <td>${item.updateTime}</td>
                                <td><a href="/sell/seller/category/index?categoryId=${item.categoryId}">修改</a></td>
                            </tr>

                            </#list>

                        </tbody>
                    </table>
                </div>

                <#--分页-无->

            </div>
        </div>
    </div>
</div>

</body>
</html>