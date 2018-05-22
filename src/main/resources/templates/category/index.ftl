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
                    <form role="form" method="post" action="/sell/seller/category/save">

                        <div class="form-group">
                            <label>名称</label>
                            <input name="categoryName" value="${(productCategory.categoryName)!''}" type="text" class="form-control" />
                        </div>
                        <div class="form-group">
                            <label>type</label>
                            <input name="categoryType" value="${(productCategory.categoryType)!''}" type="number" class="form-control" />
                        </div>

                    <#--TODO 这里使用了隐藏参数-->
                        <input hidden type="text" name="categoryId" value="${(productCategory.categoryId)!''}">
                        <button type="submit" class="btn btn-default">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>