<html>
    <head>
        <meta charset="utf-8">
        <title>卖家商品列表</title>
        <link href="https://cdn.bootcss.com/bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>订单id</th>
                            <th>姓名</th>
                            <th>手机号</th>
                            <th>地址</th>
                            <th>金额</th>
                            <th>订单状态</th>
                            <th>支付状态</th>
                            <th>创建时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>

                        <#list orderDTOPage.content as item>

                        <tr>
                            <td>${item.orderId}</td>
                            <td>${item.buyerName}</td>
                            <td>${item.buyerPhone}</td>
                            <td>${item.buyerAddress}</td>
                            <td>${item.orderAmount}</td>
                            <td>${item.orderStatus}</td>
                            <td>${item.payStatus}</td>
                            <td>${item.createTime}</td>
                            <td>详情</td>
                            <td>取消</td>
                        </tr>

                        </#list>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>