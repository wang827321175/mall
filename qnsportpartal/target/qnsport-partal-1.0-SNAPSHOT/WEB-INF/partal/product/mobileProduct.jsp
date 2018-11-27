<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    //request.setCharacterEncoding("UTF-8");
    String path = request.getContextPath();
    String basepath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">

    <title>商品列表${(store.name)}</title>
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
    </style>

</head>
<body ontouchstart>

<div class="page__bd">

    <div class="weui-cells weui-cells_form">
        <div class="weui-panel weui-panel_access">
            <div class="weui-panel__hd" style="text-align: center;font-size: 18px">商品列表</div>
        </div>
        <div class="weui-grids">

            <c:forEach items="${pageInfo.list}" var="product">
                <a href="${pageContext.request.contextPath}/product/detail.html?id=${product.id}&storeId=${store.id}"
                   target="_self" class="weui-grid">
                    <div class="weui-grid__icon">
                        <img src="${product.img.imgUrl}" alt="${product.name}">
                    </div>
                    <p class="weui-grid__label">${product.name}</p>
                </a>
            </c:forEach>
        </div>
        <br><br>
        <div class="weui-footer">
            <p class="weui-footer__links">
                <a href="javascript:void(0);" class="weui-footer__link">公司简介</a>
            </p>
            <p class="weui-footer__text">Copyright &copy; 2008-2018 武汉一张图科技有限公司</p>
        </div>
    </div>

    <!--相关条款-->
    <div class="page__ft j_bottom">

    </div>
</div>
</body>

</html>
