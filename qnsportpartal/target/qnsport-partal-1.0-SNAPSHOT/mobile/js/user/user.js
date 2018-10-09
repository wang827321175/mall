/**
 * Created by shenbiao on 2017/3/5.
 * 用户注册登录等功能
 */
//全局变量
var userName;
var userPhone;
var userPassword;
var userPasswordAgain;
var isAllRight =false;

$(function(){
    $('#dialogs').on('click', '.weui-dialog__btn', function(){
        $(this).parents('.js_dialog').fadeOut(200);
    });
});

//判断用户两次输入的密码是否一致
function isSamePassword() {
    if (userPassword == userPasswordAgain) {
        return true;
    } else {
        return false;
    }
}

//用户注册信息提交到后台
function userRegistConfirm() {
    userName = $("#userName").val();
    userPhone = $("#userPhone").val();
    userPassword = $("#userPassword").val();
    userPasswordAgain = $("#userPasswordAgain").val();
    //判断输入信息是否为空
    if(userName==null||userName==""){
        isAllRight=false;
        showErrorMsg("请输入姓名");
    }else{
        if(userPhone==null||userPhone==""){
            isAllRight=false;
            showErrorMsg("请输入手机号");
        }else{
            if(userPassword==null||userPassword==""){
                isAllRight=false;
                showErrorMsg("请输入密码");
            }else{
                if(userPasswordAgain==null||userPasswordAgain==""){
                    isAllRight=false;
                    showErrorMsg("请确认密码");
                }else{
                    if(isSamePassword()){
                        isAllRight=true;
                    }else{
                        isAllRight=false;
                        showErrorMsg("密码不一致");
                    }
                }
            }
        }
    }

    if (isAllRight) {
        //暂时不设置功能
        $('#iosDialog2').fadeIn(200);
        //把参数传递到后台
        //var param = {
        //    "userName": userName,
        //    "userPhone": userPhone,
        //    "userPassword": userPassword,
        //};
        //
        //var res = $.post(basepath+"userRegist",
        //    param,
        //    function (data) //回调函数
        //    {
        //        if(data=="success"){
        //            alert("注册成功！");
        //            window.location.href =basepath+"/login/login.jsp";
        //        }else if(data=="isExist"){
        //            showErrorMsg("注册失败，该手机号已有用户注册！");
        //        }else{
        //            showErrorMsg("注册失败，错误未知！");
        //        }
        //    }, "json"
        //).error(function () {
        //        showErrorMsg("暂时无法连接到服务器！");
        //    });
    }
}

//用户登录
function userLoginConfirm() {
    //获取输入框信息
    userPhone = $("#userPhone").val();
    userPassword = $("#userPassword").val();

    //判断输入信息是否为空

        if(userPhone==null||userPhone==""){
            isAllRight=false;
            showErrorMsg("请输入手机号")
        }else{
            if(userPassword==null||userPassword==""){
                isAllRight=false;
                showErrorMsg("请输入密码");
            }else{
                isAllRight=true;
            }
        }

    if (isAllRight) {
        if(userPhone=="00"){
            window.location.href=basepath+"navigation/navigation.jsp"
        }else if(userPhone=="11"){
            window.location.href=basepath+"a/policeDemo.jsp"
        }else{
            showErrorMsg("00为管理员账号，11为公安局账号");
        }

        ////把参数传递到后台
        //var param = {
        //    "userPhone": userPhone,
        //    "userPassword": userPassword,
        //};
        //
        //var res = $.post(basepath+"userLogin",
        //    param,
        //    function (data) //回调函数
        //    {
        //        window.location.href=basepath+"navigation/navigation.jsp"
        //    }, "json"
        //).error(function () {
        //        showErrorMsg("暂时无法连接到服务器！");
        //    });
    }
}

//显示错误提示
function showErrorMsg(errormsg) {

    var $tooltips = $("#js_tooltips");
    if ($tooltips.css('display') != 'none') return;

    // toptips的fixed, 如果有`animation`, `position: fixed`不生效
    $('.page.cell').removeClass('slideIn');

    $("#js_tooltips").html(errormsg);
    $tooltips.css('display', 'block');
    setTimeout(function () {
        $tooltips.css('display', 'none');
    }, 2000);

}

