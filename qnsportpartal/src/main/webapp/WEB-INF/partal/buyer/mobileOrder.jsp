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

<div class="page__bd">
    <div class="weui-cells weui-cells_form">
        <div class="weui-panel weui-panel_access">
            <div class="weui-panel__hd">订单结算</div>
        </div>
        <table id="myTable" border="1" cellspacing="0" cellpadding="0" style="border-color: lightgrey" width="100%">
            <tr>
                <td colspan="5" class="title">收货地址</td>
            </tr>
            <tr>
                <td colspan="5" style="text-align: left">
                    <c:forEach items="${addrList}" var="addr" varStatus="s">
                        <input type="radio" name="id" value="${addr.id}"
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
                    <input type="radio" name="payMentWay" value="0">货到付款<br>
                    <input type="radio" name="payMentWay" value="1" checked="checked">在线支付<br>
                    <input type="radio" name="payMentWay" value="2">邮局转账<br>
                    <input type="radio" name="payMentWay" value="3">公司转账<br>
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
                <tr id="tr${cart.id}">
                    <td>${cart.name}</td>
                    <td>
                        <c:forEach items="${skus}" var="sku">
                            <c:if test="${sku.productId==cart.id}">
                                <var id="price${cart.id}">${sku.price}</var>
                            </c:if>
                        </c:forEach>
                    </td>
                    <td>
                        <var>${cart.count}</var>
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
<script type="text/javascript">
    function submitOrder() {
        var total = $("#allTotalPrice").html();
        alert(total);
    }
</script>
</body>
</html>
