<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/console/head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>qingniao-add</title>
    <style type="">
        .h2_ch a:hover, .h2_ch a.here {
            color: #FFF;
            font-weight: bold;
            background-position: 0px -32px;
        }

        .h2_ch a {
            float: left;
            height: 32px;
            margin-right: -1px;
            padding: 0px 16px;
            line-height: 32px;
            font-size: 14px;
            font-weight: normal;
            border: 1px solid #C5C5C5;
            background: url('/jsutils/qingniao/img/admin/bg_ch.gif') repeat-x scroll 0% 0% transparent;
        }

        a {
            color: #06C;
            text-decoration: none;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            var tObj;
            $("#tabs a").each(function () {
                if ($(this).attr("class").indexOf("here") == 0) {
                    tObj = $(this)
                }
                $(this).click(function () {
                    var c = $(this).attr("class");
                    if (c.indexOf("here") == 0) {
                        return;
                    }
                    var ref = $(this).attr("ref");
                    var ref_t = tObj.attr("ref");
                    tObj.attr("class", "nor");
                    $(this).attr("class", "here");
                    $(ref_t).hide();
                    $(ref).show();
                    tObj = $(this);
                    if (ref == '#tab_2') {
                        var fck = new FCKeditor("productdesc");
                        fck.BasePath = "/jsutils/fckeditor/";
                        fck.Height = 400;
                        //设置上传路径
                        fck.Config["ImageUploadURL"] = "/console/upload/fckImg.do";
                        //把数据同步到 textarea
                        fck.ReplaceTextarea();
                    }
                });
            });

            var opts = {
                url: "${pageContext.request.contextPath}/img/showImg.do?id=${product.id}",
                type: "post",
                dataType: "json",
                success: function (data) {
                    $("#allimg").attr("src", data.url);
                    $("#himg").val(data.path)
                }
            };
            $("#pForm").ajaxSubmit(opts);
        });

        //图片上传
        function ImgUpload() {
            var opts = {
                url: "${pageContext.request.contextPath}/upload/fastDFSImg.do",
                type: "post",
                dataType: "json",
                success: function (data) {
                    $("#allimg").attr("src", data.url);
                    $("#himg").val(data.path)
                }
            };
            $("#pForm").ajaxSubmit(opts);
        }


    </script>
</head>
<body>
<div class="box-positon">
    <div class="rpos">当前位置: 商品管理 - 修改</div>
    <form class="ropt">
        <input type="hidden" name="storeId" value="${storeId}"/>
        <input type="submit" onclick="this.form.action='${pageContext.request.contextPath}/product/list.do';" value="返回列表" class="return-button"/>
    </form>
    <div class="clear"></div>
</div>
<h2 class="h2_ch"><span id="tabs">
<a href="javascript:void(0);" ref="#tab_1" title="基本信息" class="here">基本信息</a>
<a href="javascript:void(0);" ref="#tab_2" title="商品描述" class="nor">商品描述</a>
<a href="javascript:void(0);" ref="#tab_3" title="商品参数" class="nor">包装清单</a>
</span></h2>
<div class="body-box" style="float:right">
    <form id="pForm" action="${pageContext.request.contextPath}/product/update.do" method="post" enctype="multipart/form-data">
        <table cellspacing="1" cellpadding="2" width="100%" border="0" class="pn-ftable">
            <tbody id="tab_1">
            <input type="hidden" name="id" value="${product.id}"/>
            <input type="hidden" name="storeId" value="${storeId}"/>
            <tr>
                <td width="20%" class="pn-flabel pn-flabel-h">
                    <span class="pn-frequired">*</span>
                    商品类型:
                </td>
                <td width="80%" class="pn-fcontent">
                    <select name="typeId">
                        <option value="">请选择</option>
                        <c:forEach items="${types }" var="type">
                            <option value="${type.id }"
                                    <c:if test="${product.typeId==type.id}">selected="selected"</c:if>>${type.name}
                            </option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td width="20%" class="pn-flabel pn-flabel-h">
                    <span class="pn-frequired">*</span>
                    商品名称:
                </td>
                <td width="80%" class="pn-fcontent">
                    <input type="text" class="required" name="name" value="${product.name}" maxlength="100" size="100"/>
                </td>
            </tr>
            <tr>
                <td width="20%" class="pn-flabel pn-flabel-h">
                    品牌:
                </td>
                <td width="80%" class="pn-fcontent">
                    <select name="brandId">
                        <option value="">请选择品牌</option>
                        <c:forEach items="${brands }" var="brand">
                            <option value="${brand.id }"
                                    <c:if test="${product.brandId==brand.id}">selected="selected"</c:if>>${brand.name }
                            </option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td width="20%" class="pn-flabel pn-flabel-h">
                    商品毛重:
                </td>
                <td width="80%" class="pn-fcontent">
                    <input type="text" class="required" name="weight" value="${product.weight}" maxlength="10"/>KG
                </td>
            </tr>
            <tr>
                <td width="20%" class="pn-flabel pn-flabel-h"><span class="pn-frequired">*</span>
                    材质:
                </td>
                <td width="80%" class="pn-fcontent">

                        <c:forEach items="${features}" var="feature" varStatus="count">
                            <input type="checkbox" value="${feature.id}" name="features"
                                <c:forEach items="${pFeatures}" var="pFeature">
                                       <c:if test="${pFeature.id==feature.id}">checked="checked"</c:if>
                                </c:forEach>/>${feature.name }
                    </c:forEach>

                </td>
            </tr>
            <tr>
                <td width="20%" class="pn-flabel pn-flabel-h">
                    <span class="pn-frequired">*</span>
                    颜色:
                </td>
                <td width="80%" class="pn-fcontent">
                    <c:forEach items="${colors }" var="color">
                        <input type="checkbox" value="${color.id }" name="colors"
                            <c:forEach items="${pColors}" var="pColor">
                                <c:if test="${pColor.id==color.id}">checked="checked"</c:if>
                            </c:forEach> />${color.name }
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td width="20%" class="pn-flabel pn-flabel-h">
                    <span class="pn-frequired">*</span>
                    尺码:
                </td>
                <td width="80%" class="pn-fcontent">
                        <input type="checkbox" name="sizes" value="S"
                                <c:forEach items="${product.sizes}" var="pSize">
                                    <c:if test="${pSize=='S'}">checked="checked"</c:if>
                                </c:forEach>/>S
                        <input type="checkbox" name="sizes" value="M"
                                <c:forEach items="${product.sizes}" var="pSize">
                                    <c:if test="${pSize=='M'}">checked="checked"</c:if>
                                </c:forEach>/>M
                        <input type="checkbox" name="sizes" value="L"
                                <c:forEach items="${product.sizes}" var="pSize">
                                    <c:if test="${pSize=='L'}">checked="checked"</c:if>
                                </c:forEach>/>L
                        <input type="checkbox" name="sizes" value="XL"
                                <c:forEach items="${product.sizes}" var="pSize">
                                    <c:if test="${pSize=='XL'}">checked="checked"</c:if>
                                </c:forEach>/>XL
                        <input type="checkbox" name="sizes" value="XXL"
                                <c:forEach items="${product.sizes}" var="pSize">
                                    <c:if test="${pSize=='XXL'}">checked="checked"</c:if>
                                </c:forEach>/>XXL
                </td>
            </tr>
            <tr>
                <td width="20%" class="pn-flabel pn-flabel-h">
                    状态:
                </td>
                <td width="80%" class="pn-fcontent">
                    <input type="checkbox" name="isNew" value="1"
                           <c:if test="${product.isNew==true}">checked="checked"</c:if>/>新品
                    <input type="checkbox" name="isCommend" value="1"
                           <c:if test="${product.isCommend==true}">checked="checked"</c:if>/>推荐
                    <input type="checkbox" name="isHot" value="1"
                           <c:if test="${product.isHot==true}">checked="checked"</c:if>/>热卖
                </td>
            </tr>
            <tr>
                <td width="20%" class="pn-flabel pn-flabel-h">
                    <span class="pn-frequired">*</span>
                    上传商品图片(100x100尺寸):
                </td>
                <td width="80%" class="pn-fcontent">
                    注:该尺寸图片建议为100x100。
                </td>
            </tr>
            <tr>
                <td width="20%" class="pn-flabel pn-flabel-h"></td>
                <td width="80%" class="pn-fcontent">
                    <img width="100" height="100" id="allimg"/>
                    <input type="file" name="picfile" onchange="ImgUpload();"/>
                    <input type="hidden" name="img.url" value="${imgUrl}" id="himg"/>
                </td>

            </tr>
            </tbody>
            <tbody id="tab_2" style="display: none">
            <tr>
                <td>
                    <textarea rows="10" cols="10" id="productdesc" name="description">${product.description}</textarea>
                </td>
            </tr>
            <tr>
                <td>
                   <font color="red">*上传图片时请将图像宽度调整为480。</font>
                </td>
            </tr>
            </tbody>
            <tbody id="tab_3" style="display: none">
            <tr>
                <td>
                    <textarea rows="15" cols="136" id="productList" name="packageList">${product.packageList}</textarea>
                </td>
            </tr>
            </tbody>
            <tbody>
            <tr>
                <td class="pn-fbutton" colspan="2">
                    <input type="submit" class="submit" value="提交"/> &nbsp; <input type="reset" class="submit"
                                                                                   value="重置"/>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
</div>
</body>
</html>