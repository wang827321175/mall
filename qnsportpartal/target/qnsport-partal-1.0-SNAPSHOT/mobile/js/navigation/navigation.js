/**
 * Created by shenbiao on 2017/3/5.
 * 导航栏功能
 */

$(function () {
    /**
     * 点击按钮修改样式
     */
    $('.weui-tabbar__item').on('click', function () {
        $(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
    });

});

/**
 * 导航栏
 */
//点击二维码管理
function showqrcode() {
    $(".pageContent").css("display","none");
    $("#tpl_qrcode").css("display","block");

}

//点击服务管理
function showManagement() {
    //加载大类服务
    getBigserviceBySmallDistCode();
    //页面显示
    $(".pageContent").css("display","none");
    $("#tpl_management").css("display","block");
}
//点击服务监控
function showMonitoring() {
    $(".pageContent").css("display","none");
    $("#tpl_monitoring").css("display","block");
}
//点击市民反馈
function showFeedback() {
    $(".pageContent").css("display","none");
    $("#tpl_feedback").css("display","block");
}

