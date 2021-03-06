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

    <title>订单结算页</title>
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
            height: 30px;
        }

        .title {
            background: #9dcaf3;
        }

    </style>

</head>
<body ontouchstart>
<input type="hidden" id="username" name="username" value="${username}"/>
<div class="page__bd">
    <div class="weui-cells weui-cells_form">
        <div class="weui-panel weui-panel_access">
            <div class="weui-panel__hd" style="font-size: 20px;height: 18px">
                <div style="width: 40px;float:left"><a href="javascript:history.back(-1)">返回</a></div>
                <div style="width: 220px;float:right" text-align="center">订单结算</div>
            </div>
        </div>
        <table id="myTable" border="1" cellspacing="0" cellpadding="0" style="border-color: lightgrey" width="100%">
            <tr>
                <td colspan="5" class="title">收货地址</td>
            </tr>
            <tr>
                <td colspan="5" style="text-align: left">
                    <c:forEach items="${addrList}" var="addr" varStatus="s">
                        <input type="radio" name="addr" value="${addr.id}"
                               <c:if test="${s.count==1}">checked="checked"</c:if>>
                        &nbsp;&nbsp;
                        ${addr.name}
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        ${addr.phone}
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        ${addr.city}
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td colspan="5" class="title">支付方式</td>
            </tr>
            <tr>
                <td colspan="5" style="text-align: left">
                    <input type="radio" name="payMentWay" value="0" onchange="displayInputBox()">货到付款<br>
                    <input type="radio" name="payMentWay" value="1" checked="checked"
                           onchange="displayInputBox()">在线支付<br>
                    <input type="radio" name="payMentWay" value="2" onchange="displayInputBox()">邮局转账<br>
                    <input type="radio" name="payMentWay" value="3" onchange="displayInputBox()">公司转账<br>
                </td>
            </tr>
            <tr id="cashOnDeliveryTitle" style="display: none">
                <td colspan="5" class="title">货到付款支付方式</td>
            </tr>
            <tr id="cashOnDelivery" style="display: none">
                <td colspan="5" style="text-align: left">
                    <input type="radio" id="payMentCash1" name="payMentCash" value="1">现金<br>
                    <input type="radio" name="payMentCash" value="2">POS刷卡<br>
                </td>
            </tr>
            <tr>
                <td colspan="5" class="title">商品清单</td>
            </tr>
            <td width="30%">商品名称</td>
            <td width="13%">价格</td>
            <td width="20%">数量</td>
            <td width="15%">详情</td>
            <td width="15%">合计</td>
            <c:forEach items="${carts}" var="cart">
                <input id="cart${cart.id}" name="cartId" type="hidden" value="${cart.id}"/>
                <tr id="tr${cart.id}">
                    <td>
                        <var id="name${cart.id}">
                                ${cart.name}
                        </var>
                    </td>
                    <td>
                        <c:forEach items="${skus}" var="sku">
                            <c:if test="${sku.productId==cart.id}">
                                <var id="price${cart.id}">${sku.price}</var>
                            </c:if>
                        </c:forEach>
                    </td>
                    <td>
                        <var id="count${cart.id}">${cart.count}</var>
                    </td>
                    <td>
                        <var id="size${cart.id}">
                            <c:forEach items="${products}" var="product">
                                <c:if test="${product.id==cart.id}">
                                    ${product.sizes}
                                </c:if>
                            </c:forEach>
                        </var>
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
                </tr>
            </c:forEach>
            <tr>
                <td colspan="5" class="title">订单总计</td>
            </tr>
            <c:if test="${totalPrice>0}">
                <tr>
                    <td colspan="5" style="text-align: left">
                        商品价格:
                        <var id="totalPrice">
                            <fmt:formatNumber type="number" value="${totalPrice}" maxFractionDigits="1"/>元
                        </var><br>

                        运费:
                        <c:if test="${totalPrice>=99}">
                        <var id="deliver"><fmt:formatNumber type="number" value="0" maxFractionDigits="1"/></var>元<br>
                        价格总计:
                        <var id="allTotalPrice">
                            <fmt:formatNumber type="number" value="${totalPrice+0}" maxFractionDigits="1"/>
                        </var>元
                        </c:if>

                        <c:if test="${totalPrice<99}">
                        <var id="deliver"><fmt:formatNumber type="number" value="9" maxFractionDigits="1"/></var>元<br>
                        价格总计:
                        <var id="allTotalPrice"><fmt:formatNumber type="number" value="${totalPrice+9}"
                                                                  maxFractionDigits="1"/></var>元
                        </c:if>
                </tr>
            </c:if>
        </table>
    </div>
    <br><br>
    <a class="weui-btn weui-btn_primary" onclick="submitOrder()">提交订单</a>
    <a href="${pageContext.request.contextPath}/index.html?storeId=${storeId}"
       class="weui-btn weui-btn_primary">返回继续购物</a>

    <br>
    <div class="weui-footer">
        <p class="weui-footer__links">
            <a href="javascript:void(0);" class="weui-footer__link">公司简介</a>
        </p>
        <p class="weui-footer__text">Copyright &copy; 2008-2018 武汉一张图科技有限公司</p>
    </div>
</div>
<script>
    function submitOrder() {
        var total = $("#allTotalPrice").html();//总价
        var deliver = $("#deliver").html();//运费
        var addr = $("input[name='addr']:checked").val();//地址编号
        var payMentWay = $("input[name='payMentWay']:checked").val();//支付方式
        var payMentCash = $("input[name=payMentCash]:checked").val();//货到付款支付方式
        var username = $("#username").val();//用户名
        //生成订单

        //保存订单详情
        var cartIds = new Array();
        var names = new Array();
        var prices = new Array();
        var counts = new Array();
        var sizes = new Array();
        var val = $("input[name='cartId']").each(function (j, item) {
            var cartId = item.value;
            cartIds.push(cartId);//购物车中每项的id和商品id一致
            var name = $("#name" + cartId).html();
            names.push(name);
            var price = $("#price" + cartId).html();
            prices.push(price);
            var count = $("#count" + cartId).html();
            counts.push(count);
            var size = $("#size" + cartId).html();
            sizes.push(size);
        });
        console.log(cartIds)
        var url = "${pageContext.request.contextPath}/order/mobileSubmitOrder.html";
        var pram = {
            "totalFee": total,
            "deliverFee": deliver,
            "isConfirm": addr,
            "paymentWay": payMentWay,
            "paymentCash": payMentCash,
            "buyerId": username,

            "cartIds": cartIds,
            "names": names,
            "prices": prices,
            "counts": counts,
            "sizes": sizes
        }

        $.post(url, pram, function (data) {
            var data1 = eval("(" + data + ")");
            console.log("Order added successfully 订单号为" + data1.id)
        })
    }


    function displayInputBox() {
        var payMentWay = $("input[name='payMentWay']:checked").val();
        if (payMentWay == 0) {
            $("#cashOnDeliveryTitle").css("display", "");
            $("#cashOnDelivery").css("display", "");
            $("#payMentCash1").attr("checked", "checked");
        } else {
            $("#cashOnDeliveryTitle").css("display", "none");
            $("#cashOnDelivery").css("display", "none");
            $("#payMentCash1").removeAttr("checked");
        }
    }
</script>
</body>
</html>
