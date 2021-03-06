<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    request.setCharacterEncoding("UTF-8");
    String path = request.getContextPath();
    String basepath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">

    <title>智慧门牌</title>
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

        #input {
            /*width: 250px;*/
            height: 25px;
            border: 0;
            background-color: white;
        }

        #description {
            width: 640px;
        }

        .w2 {
            letter-spacing: 1em; /*如果需要y个字两端对齐，则为(x-y)/(y-1),这里是（4-2）/(2-1)=2em */
            margin-right: -1em; /*同上*/
            text-align: center;
        }

        td {
            height: 30px;
        }
    </style>

</head>
<body>

<div class="page__bd">


    <div class="weui-cells weui-cells_form">
        <div class="weui-panel weui-panel_access" style="font-size: 20px">
            <div style="width: 40px;float: left"><a href="javascript:history.back(-1)">返回</a></div>
            <div style="width: 220px;float:right" text-align="center">购物车列表</div>
        </div>

        <div class="weui-grids">
            <div>
                <img src="${product.img.imgUrl}" width="480px" height="480px" alt=""/>
            </div>
            <p class="weui-grid__label">
            <h2 align="center">${product.name}</h2></p>
        </div>


        <div class="weui-grids">
            <p>
            <table border="1" cellspacing="0" cellpadding="0" style="border-color: lightgrey" width="100%">
                <tr>
                    <td width="15%">
                        <div class="w2">品牌</div>
                    </td>
                    <td>
                        <c:forEach items="${brands}" var="brand">
                            <c:if test="${product.brandId==brand.id}">${brand.name}</c:if>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="w2">颜色</div>
                    </td>
                    <td>
                        <c:forEach items="${allColors}" var="color">
                            <c:forEach items="${product.colors}" var="pColor">
                                <c:if test="${pColor==color.id}">${color.name}</c:if>
                            </c:forEach>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="w2">尺码</div>
                    </td>
                    <td>
                        <c:forEach items="${product.sizes}" var="size">
                            ${size}&nbsp;
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="w2">材质</div>
                    </td>
                    <td>
                        <c:forEach items="${features}" var="feature">
                            <c:forEach items="${product.features}" var="pFeature">
                                <c:if test="${pFeature==feature.id}">
                                    ${feature.name}&nbsp;
                                </c:if>
                            </c:forEach>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="w2">重量</div>
                    </td>
                    <td>
                        ${product.weight}kg
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="w2">类型</div>
                    </td>
                    <td>
                        <c:forEach items="${types}" var="type">
                            <c:if test="${product.typeId==type.id}">${type.name}</c:if>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="w2">清单</div>
                    </td>
                    <td>
                        ${product.packageList}
                    </td>
                </tr>
            </table>
            </p>
        </div>
        <br>
        <%--购买链接--%>
        <a href="javascript:void(0)" onclick="addMobileCart(${product.id})"
           class="weui-btn weui-btn_primary">加入购物车</a>
        <a href="${pageContext.request.contextPath}/cart/goToCart.html?storeId=${product.storeId}&username=tom"
           class="weui-btn weui-btn_primary">查看购物车</a>
        <br>
        <%--商品描述--%>
        <div id="description" class="weui-grids">
            <p class="weui-grid__label">
                ${product.description}
            </p>
        </div>

        <br><br>
        <div class="weui-footer">
            <p class="weui-footer__links">
                <a href="javascript:void(0);" class="weui-footer__link">公司简介</a>
            </p>
            <p class="weui-footer__text">Copyright &copy; 2008-2018 武汉一张图科技有限公司</p>
        </div>
    </div>
</div>
</body>

<script>
    function addMobileCart(id) {
        var url = "${pageContext.request.contextPath}/cart/addToCart.html";
        var params = {"id": id};
        $.post(url, params, function (data) {
            var data = eval("(" + data + ")");
            alert(data.msg);

        });
    }
</script>


</html>
