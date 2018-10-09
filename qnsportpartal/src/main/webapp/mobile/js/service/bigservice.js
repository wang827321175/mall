/**
 * Created by shenbiao on 2017/3/5.
 * 导航栏功能
 */

var timeout=0;
var bigserviceLiId;//大类服务的li的id
var bigservicePId;//删除，之后是，修改

//function pressToDelete(){
//
//}

//var timeOutEvent=0;
$(function(){

    /**
     * 移动事件
     */
    // List with handle
    Sortable.create(originBigService, {
        group: 'origin',
        handle: '.glyphicon-move',
        animation: 150,
        onStart: function (/**Event*/evt) {
            $("#ashbin").css("display","block");
            bigserviceLiId=evt.item.id;  // element index within parent
        },
        onEnd: function (/**Event*/evt) {
            $("#ashbin").css("display","none");
        },
    });

    //可以移动到垃圾堆哦~
    Sortable.create(ashbin, {
        group: {
            name: 'ashbin',
            put:putIntoAshbin,
        },
        animation: 100
    });

    /**
     * 电脑端拖动删除
     */
    $(".pressToDelete").mousedown(function (e) {
        bigservicePId= e.target.id;
        timeout = setTimeout("longPress()", 500);
    });

    $(".pressToDelete").mouseup(function () {
        clearTimeout(timeout);
        //$("#mydiv").text("out");
    });

    $(".pressToDelete").mouseout(function () {
        clearTimeout(timeout);
        //$("#mydiv").text("out");
    });

    /**
     * 移动端拖动删除
     */
    $(".pressToDelete").on({
        touchstart: function(e){
            bigservicePId= e.target.id;
            timeout = setTimeout("longPress()", 500);
            //e.preventDefault();
        },
        touchmove: function(){
            clearTimeout(timeout);
            timeout = 0;
        },
        touchend: function(){
            clearTimeout(timeout);
        }
    })
});

/**
 * 根据街道id,按照顺序加载出大类服务
 */
function getBigserviceBySmallDistCode(){
    var param={
        smallDistCode:"330702006001",
    }
    $.ajax({
        type:"POST",
        data:param,
        url:basepath+"/serviceAction!getBigserviceBySmallDistCode",
        dataType:"json",
        success:function(data){
            $("#originBigService").empty();
            $.each(data,function (i,item){
                var liHTML='<li id="bigService_li_'+item.id+'"><div class="weui-flex js_category">' +
                    '<p class="weui-flex__item pressToDelete" id="bigService_'+item.id+'">'+item.serviceName+'</p>' +
                    '<img class="glyphicon-move" src="'+basepath+'/images/icon_nav_form.png" alt="">' +
                    '</div><div class="page__category js_categoryInner"><div class="weui-grids page__category-content"> ' +
                    '</div></div></li>'
                $("#originBigService").append(liHTML);
            })
        }
    });
}


/**
 * 增加大服务哦
 */
function addBigService(){
    var newBigService='<li id=""> ' +
        '<div class="weui-flex js_category"> <p class="weui-flex__item pressToDelete" id="">新大类服务</p> <img class="glyphicon-move" src="'+basepath+'/images/icon_nav_form.png" alt=""> </div> ' +
        '<div class="page__category js_categoryInner"> <div class="weui-grids page__category-content"> ' +
        '<a href="javascript:;" class="weui-grid"> <div class="weui-grid__icon"> <img src="'+basepath+'/images/plus_32px_581648_easyicon.net.png" alt=""> </div> <p class="weui-grid__label">添加子服务</p> </a> ' +
        '</div> </div></li>'
    $("#originBigService").append(newBigService);
}

/**
 *修改大服务
 */

function longPress(){
    timeout = 0;
    showIosDialogInput("修改大类服务名","请输入新大类服务名",editBigService);
}

function editBigService(){
    //bigserviceLiId用这个去数据库里面删除
    var newBigServiceName=$("#iosDialog_input_content").val();
    $("#"+bigservicePId).text(newBigServiceName);
    $("#iosDialog_input_content").val("");
    //$(".sortable-chosen").remove();
}

/**
 *删除大服务
 */

function putIntoAshbin(){
    if(browser.versions.mobile || browser.versions.ios || browser.versions.android ||
        browser.versions.iPhone || browser.versions.iPad){
        $("#ashbin").css("display","none");
        //showActionDialog("提示","yi東段",deleteBigService);
        return false;
    }else{
        showActionDialog("提示","是否确认删除该大类服务",deleteBigService);
        //$("#ashbin").css("display","none");
        return false;
    }

}

function deleteBigService(){
    //bigserviceLiId用这个去数据库里面删除
    $("#"+bigserviceLiId).remove();
    //$(".sortable-chosen").remove();
}



