<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>我的购物车</title>
    <link rel="stylesheet" href="/jsutils/css/style.css"/>
    <script src="/jsutils/js/jquery.js"></script>
    <script src="/jsutils/js/com.js"></script>
    <script type="text/javascript">

        //删除
        function delProduct(skuId) {
            var message = window.confirm("确定删除吗?")
            if(message){
                window.location.href = "/shopping/delProduct.html?skuId=" + skuId;
            }else{
                return;
            }

        }

        //商品追加
        function addProductAmount(skuId, upperLimit) {
            var num = $("#num" + skuId).val();
            num++;
            //判断是否大于购买限制
            if (num > upperLimit) {
                alert("已达到购买上限");
                return;
            }
            // 修改增加的数量
            $("#num" + skuId).val(num);
            //使用ajax异步提交
            var url = "/shopping/addAmount.html";
            var params = {"skuId": skuId, "amount": 1};
            $.post(url, params, function (data) {
                var data = eval("(" + data + ")");	//把json字符串转换成json对象
                //商品总计
                $("#productAmount").html(data.productAmount)
                //运费
                $("#extra").html(data.extra);
                //商品价格
                $("#price").html(data.price);
                //商品总价
                $("#allPrice").html(data.allPrice);
            });
        }

        //减少商品购买数量
        function subProductAmount(skuId) {
            var num = $("#num" + skuId).val();
            num--;
            if (num == 0) {
                alert("至少购买一件");
                return;
            }

            //替换数据
            $("#num" + skuId).val(num);

            //异步提交
            var url = "/shopping/addAmount.html";
            var params = {"skuId": skuId, "amount": "-1"};
            $.post(url, params, function (data) {
                var data = eval("(" + data + ")");
                //商品总计
                $("#productAmount").html(data.amount)
                //运费
                $("#extra").html(data.extra);
                //商品价格
                $("#price").html(data.price);
                //商品总价
                $("#allPrice").html(data.allPrice);

            });
        }

        //清空购物车
        function clearCart() {
            //页面刷新
            window.location.href = "/shopping/clearCart.html";
        }

        //结算
        function toBuy() {
            window.location.href = "/buy/balanceAccounts.html?url=" + window.location.pathname;
        }

    </script>
</head>
<body>
<div class="bar">
    <div class="bar_w">
        <p class="l">
		<span class="l">
			收藏本网站！北京<a href="#" title="更换">[更换]</a>
		</span>
        </p>
        <ul class="r uls">
            <li class="dev">
                您好,欢迎来到青鸟运动购物平台！
            </li>
            <li class="dev"><a href="javascript:void(0)" onclick="login()" title="登陆">[登陆]</a></li>
            <li class="dev"><a href="javascript:void(0)" onclick="register()" title="免费注册">[免费注册]</a></li>
            <li class="dev"><a href="javascript:void(0)" onclick="logout()" title="退出">[退出]</a></li>
            <li class="dev"><a href="javascript:void(0)" onclick="myOrder()" title="我的订单">我的订单</a></li>
            <li class="dev"><a href="#" title="在线客服">在线客服</a></li>
            <li class="dev after"><a href="#" title="English">English</a></li>
        </ul>
    </div>
</div>
<ul class="ul step st3_1">
    <li title="1.我的购物车" class="here">1.我的购物车</li>
    <li title="2.填写核对订单信息">2.填写核对订单信息</li>
    <li title="3.成功提交订单">3.成功提交订单</li>
</ul>
<c:if test="${userCart.items!=null}">
    <div class="w ofc case">
        <div class="confirm">
            <div class="tl"></div>
            <div class="tr"></div>
            <div class="ofc pb40">

                <div class="page">
                    <b class="l f14 blue pt48">
                        我挑选的商品：
                    </b>
                </div>
                <table cellspacing="0" class="tab tab4" summary="">
                    <thead>
                    <tr>
                        <th class="wp">商品</th>
                        <th>单价（元）</th>
                        <th>数量</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${userCart.items }" var="item">
                        <tr>
                            <td class="nwp pic">
                                <ul class="uls">
                                    <li>
                                        <a class="pic" title="#" href="#"><img alt="#"
                                                                               src="${item.sku.product.img.imgUrl }"></a>
                                        <dl>
                                            <dt><a title="#"
                                                   href="#"> ${item.sku.product.name }--${item.sku.color.name }--${item.sku.size }</a>
                                            </dt>
                                            <dd><span class="red">【赠品】</span>
                                                <p class="box_d bg_gray2 gray"><a title="腕带" href="#">腕带</a><br></p>
                                            </dd>
                                        </dl>
                                    </li>
                                </ul>
                            </td>
                            <td>￥${item.sku.price }</td>
                            <td>
                                <a onclick="subProductAmount('${item.sku.id}')" class="inb arr" title="减"
                                   href="javascript:void(0);">-</a>
                                <input type="text" id="num${item.sku.id }" readonly="readonly"
                                       value="${item.amount }" name="" size="1" class="txts">
                                <a onclick="addProductAmount('${item.sku.id}','${item.sku.upperLimit}')"
                                   class="inb arr" title="加" href="javascript:void(0);">+</a>
                                <dl style="margin-left: 20px;color: red">
                                    <c:if test="">有货</c:if>
                                    <c:if test="">无货</c:if>
                                </dl>
                            </td>
                            <td class="blue"><a onclick="delProduct('${item.sku.id }')" title="删除"
                                                href="javascript:void(0);">删除</a></td>
                        </tr>

                    </c:forEach>
                    </tbody>
                </table>
                <div class="page">
				<span class="l">
					<input type="button" onclick="window.open('http://localhost:8083')" class="hand btn100x26c"
                           title="继续购物" value="继续购物">
					<input type="button" onclick="clearCart()" class="hand btn100x26c" title="清空购物车" value="清空购物车">
				</span>
                    <span class="r box_gray">
					<dl class="total">
                            <dt>购物车金额小计：<cite>(共<var id="productAmount">${userCart.productAmount}</var>个商品)</cite></dt>
                                <dd><em class="l">商品金额：</em>￥<var id="price">${userCart.price}</var>元</dd>
                                <dd><em class="l">运费：</em>￥<var id="extra">${userCart.extra}</var></dd>
                                <dd class="orange"><em class="l">应付总额：</em>￥<var
                                        id="allPrice">${userCart.allPrice}</var>元</dd>
                                <dd class="alg_c"><input type="button" onclick="toBuy();" class="hand btn136x36a"
                                                         value="结算" id="settleAccountId"></dd>
                    </dl>
				</span>
                </div>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${userCart.items==null}">
    <div class="w ofc case">
        <div class="confirm">
            <div class="tl"></div>
            <div class="tr"></div>
            <div class="ofc pb40" style="text-align: center;height: 200px;margin-top: 80px">
                <a href="http://localhost:8081" style="color: red;font-size: 30px;">去首页</a>挑选喜欢的商品
            </div>
        </div>
    </div>

</c:if>
<div class="mode">
    <div class="tl"></div>
    <div class="tr"></div>
    <ul class="uls">
        <li class="first">
            <span class="guide"></span>
            <dl>
                <dt title="购物指南">购物指南</dt>
                <dd><a href="#" title="购物流程">购物流程</a></dd>
                <dd><a href="#" title="购物流程">购物流程</a></dd>
                <dd><a href="#" target="_blank" title="联系客服">联系客服</a></dd>
                <dd><a href="#" target="_blank" title="联系客服">联系客服</a></dd>
            </dl>
        </li>
        <li>
            <span class="way"></span>
            <dl>
                <dt title="支付方式">支付方式</dt>
                <dd><a href="#" title="货到付款">货到付款</a></dd>
                <dd><a href="#" title="在线支付">在线支付</a></dd>
                <dd><a href="#" title="分期付款">分期付款</a></dd>
                <dd><a href="#" title="分期付款">分期付款</a></dd>
            </dl>
        </li>
        <li>
            <span class="help"></span>
            <dl>
                <dt title="配送方式">配送方式</dt>
                <dd><a href="#" title="上门自提">上门自提</a></dd>
                <dd><a href="#" title="上门自提">上门自提</a></dd>
                <dd><a href="#" title="上门自提">上门自提</a></dd>
                <dd><a href="#" title="上门自提">上门自提</a></dd>
            </dl>
        </li>
        <li>
            <span class="service"></span>
            <dl>
                <dt title="售后服务">售后服务</dt>
                <dd><a href="#" target="_blank" title="售后策略">售后策略</a></dd>
                <dd><a href="#" target="_blank" title="售后策略">售后策略</a></dd>
                <dd><a href="#" target="_blank" title="售后策略">售后策略</a></dd>
                <dd><a href="#" target="_blank" title="售后策略">售后策略</a></dd>
            </dl>
        </li>
        <li>
            <span class="problem"></span>
            <dl>
                <dt title="特色服务">特色服务</dt>
                <dd><a href="#" target="_blank" title="夺宝岛">夺宝岛</a></dd>
                <dd><a href="#" target="_blank" title="夺宝岛">夺宝岛</a></dd>
                <dd><a href="#" target="_blank" title="夺宝岛">夺宝岛</a></dd>
                <dd><a href="#" target="_blank" title="夺宝岛">夺宝岛</a></dd>
            </dl>
        </li>
    </ul>
</div>
</body>
</html>