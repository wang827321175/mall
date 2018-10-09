<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    //request.setCharacterEncoding("UTF-8");
    String path = request.getContextPath();
    String basepath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">

    <title>购物车列表</title>
    <link rel="stylesheet" type="text/css" href="<%=basepath%>/mobile/style/weui.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basepath%>/mobile/style/example.css"/>
    <script type="text/javascript" src="<%=basepath%>/mobile/js/jquery-1.10.2.min.js"></script>

    <style type="text/css">
        body, html, #container {
            height: 100%;
            margin: 0px;
        }

        .panel {
            background-color: #ddf;
            color: #333;
            border: 1px solid silver;
            box-shadow: 3px 4px 3px 0px silver;
            position: absolute;
            top: 10px;
            right: 10px;
            border-radius: 5px;
            overflow: hidden;
            line-height: 20px;
        }

        input.text {
            text-align: center;
            background-color: white;
        }

        th, td {
            text-align: center;
        }
    </style>

</head>
<body ontouchstart>

<div class="page__bd">
    <div class="weui-cells weui-cells_form">
        <div class="weui-panel weui-panel_access">
            <div class="weui-panel__hd">购物车列表</div>
        </div>
        <c:if test="${totalPrice==0}">
            <p style="text-align: center;line-height: 40px;height: 35px">您的购物车中还未添加商品</p>
        </c:if>
        <c:if test="${totalPrice!=0}">
            <table id="myTable" border="1" cellspacing="0" cellpadding="0" style="border-color: lightgrey" width="100%">


                <tr>
                    <th height="30px" width="30%">商品名称</th>
                    <th width="13%">价格</th>
                    <th width="20%">数量</th>
                    <th width="15%">详情</th>
                    <th width="15%">合计</th>
                    <th>操作</th>
                </tr>


                <c:forEach items="${carts}" var="cart">
                    <tr id="tr${cart.id}">
                        <td height="30px">${cart.name}</td>
                        <td>
                            <c:forEach items="${skus}" var="sku">
                                <c:if test="${sku.productId==cart.id}">
                                    <var id="price${cart.id}">${sku.price}</var>
                                </c:if>
                            </c:forEach>
                        </td>
                        <td>
                            <a href="#" onclick="subProductAmount(${cart.id},'${cart.username}')">－</a>
                            <input type="text" class="text" id="amount${cart.id}" value="${cart.count}"
                                   readonly="readonly"
                                   size="2"/>
                            <a href="#" onclick="addProductAmount(${cart.id},'${cart.username}')">＋</a>
                        </td>
                        <td>
                            <c:forEach items="${products}" var="product">
                                <c:if test="${product.id==cart.id}">
                                    ${product.sizes}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach items="${pPriceTotal}" var="item">
                                <c:if test="${item.key==cart.id}">
                                    <var id="pPrice${cart.id}">
                                        <fmt:formatNumber type="number" value="${item.value}" maxFractionDigits="1"/>
                                    </var>
                                </c:if>
                            </c:forEach>
                        </td>
                        <td>
                            <a href="#" onclick="delProduct(${cart.id},'${cart.username}');
                                    reCalculation('${cart.username}')">删除</a>
                            <c:set var="username" value="${cart.username}"/>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${totalPrice>0}">
                    <tr>
                        <td colspan="6" height="30px">价格合计:
                            <var id="totalPrice">
                                <fmt:formatNumber type="number" value="${totalPrice}" maxFractionDigits="1"/>
                            </var>
                    </tr>
                </c:if>
            </table>
        </c:if>
    </div>
    <br><br>

    <a href="${pageContext.request.contextPath}/index.html?storeId=${storeId}"
       class="weui-btn weui-btn_primary">返回继续购物</a>
    <a href="${pageContext.request.contextPath}/cart/toOrderPage.html?username=${username}&storeId=${storeId}"
       class="weui-btn weui-btn_primary">结算</a>


    <!--相关条款-->

    <br>
    <div class="weui-footer">
        <p class="weui-footer__links">
            <a href="javascript:void(0);" class="weui-footer__link">公司简介</a>
        </p>
        <p class="weui-footer__text">Copyright &copy; 2008-2018 武汉一张图科技有限公司</p>
    </div>

</div>


<script src="https://webapi.amap.com/maps?v=1.3&amp;key=0527fc08a6b9ab7a0d2dacdf50ed20d6&callback=init"></script>
<!-- UI组件库 1.0 -->
<script src="//webapi.amap.com/ui/1.0/main.js"></script>
<script type="text/javascript" src="https://webapi.amap.com/demos/js/liteToolbar.js"></script>
<script>
    //增加购物车数量
    function addProductAmount(id, username) {
        var num = $("#amount" + id).val();
        num++;
        if (num == 0) {
            alert("至少购买一件");
            return;
        }

        //替换数据
        $("#amount" + id).val(num);
        var url = "${pageContext.request.contextPath}/cart/addAmount.html";
        var params = {"id": id, "amount": num, "username": username};
        $.post(url, params, function (data) {
            var data = eval("(" + data + ")");
            $("#totalPrice").html(data.totalPrice);
        });

        //获取单价和数量
        var price = $("#price" + id).html();
        var count = $("#amount" + id).val();
        $("#pPrice" + id).html(price * count);


    }

    //减少购物车数量
    function subProductAmount(id, username) {
        var num = $("#amount" + id).val();
        num--;
        if (num == 0) {
            alert("至少购买一件");
            return;
        }
        //替换数据
        $("#amount" + id).val(num);
        var url = "${pageContext.request.contextPath}/cart/addAmount.html";
        var params = {"id": id, "amount": num, "username": username};
        $.post(url, params, function (data) {
            var data = eval("(" + data + ")");
            $("#totalPrice").html(data.totalPrice);
        });

        //获取单价和数量
        var price = $("#price" + id).html();
        var count = $("#amount" + id).val();
        $("#pPrice" + id).html(price * count);
    }

    //给删除绑定点击事件,重新计算总价
    function reCalculation(username) {
        var url = "${pageContext.request.contextPath}/cart/reCalculation.html";
        var params = {"username": username};
        $.post(url, params, function (data) {
            var data = eval("(" + data + ")");
            $("#totalPrice").html(data.totalPrice);
        });
    }

    //删除
    function delProduct(id, username) {
        var message = window.confirm("确定删除吗?")
        if (message) {
            //window.location.href = "/cart/deleteCart.html?id=" + id + "&username=" + username + "&storeId=" + storeId;
            $.post('${pageContext.request.contextPath}/cart/deleteCart.html', {id: id, username: username},
                function (data) {
                    if (data.msg == "success") {
                        $("#tr" + id).remove();
                    }
                }, "json")
        } else {
            return;
        }
    }

</script>

</body>
</html>
