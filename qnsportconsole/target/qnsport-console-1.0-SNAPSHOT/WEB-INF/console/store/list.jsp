<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/console/head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>qingniao-list</title>
    <script type="text/javascript">
        function getTableForm() {
            return document.getElementById('tableForm');
        }

        function optDelete() {
            if (Pn.checkedCount('ids') <= 0) {
                alert("请至少选择一个!");
                return;
            }
            if (!confirm("确定删除吗?")) {
                return;
            }
            var f = getTableForm();
            f.action = "${pageContext.request.contextPath}/store/batchDelete.do";
            f.submit();
        }

        //商品上架
        function onSale() {
            var length = $("input[name='ids']:checked").size();
            if (length == 0) {
                alert("至少选择一个")
            }
            if (!confirm("确定上架吗?")) {
                return;
            }
            $("#tableForm").attr("action", "${pageContext.request.contextPath}/product/onSale.do").submit();
        }

        //商品下架
        function sellOut() {
            var length = $("input[name='ids']:checked").size();
            if (length == 0) {
                alert("请至少选择一个")
            }
            if (!confirm("确定下架吗?")) {
                return;
            }
            $("#tableForm").attr("action", "${pageContext.request.contextPath}/product/sellOut.do").submit();
        }
    </script>
</head>
<body>
<div class="box-positon">
    <div class="rpos">当前位置: 店铺管理 - 列表</div>
    <form class="ropt">
        <input class="add" type="button" value="添加"
               onclick="javascript:window.location.href='${pageContext.request.contextPath}/store/add.do?storeId=${storeId}'"/>
    </form>
    <div class="clear"></div>
</div>
<div class="body-box">
    <form method="post" id="tableForm">
        <table cellspacing="1" cellpadding="0" width="100%" border="0"
               class="pn-ltable">
            <thead class="pn-lthead">
            <tr>
                <th width="20"><input type="checkbox" onclick="Pn.checkbox('ids',this.checked)"/></th>
                <%--<th>商铺编号</th>--%>
                <th>店铺名称</th>
                <th>描述</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody class="pn-ltbody">
            <c:forEach items="${storeList}" var="s">
                <tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'"
                    onmouseout="this.bgColor='#ffffff'">
                    <td><input type="checkbox" name="ids" value="${s.id }"></td>
                    <%--<td>${s.id }</td>--%>
                    <td>${s.name }</td>
                    <td>${s.description}</td>
                    <td><a href="${pageContext.request.contextPath}/product/list.do?storeId=${s.id}">查看商品</a>|
                        <a href="${pageContext.request.contextPath}/store/toUpdatePage.do?storeId=${s.id}">修改信息</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <div style="margin-top: 15px;">
            <input class="del-button" type="button" value="删除"
                   onclick="optDelete();"/><%--<input class="add" type="button"
                                                  value="上架" onclick="onSale();"/><input class="del-button"
                                                                                         type="button" value="下架"
                                                                                         onclick="sellOut();"/>--%>
        </div>
    </form>
</div>
</body>
</html>