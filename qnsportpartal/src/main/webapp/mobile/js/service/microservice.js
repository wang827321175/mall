/**
 * Created by shenbiao on 2017/3/5.
 * 微服务
 */

$(function(){
    addClickFunction();
})

function addClickFunction(){
    $(".weui-grid").click(function(evt){
        var targetWeuiGrid=this;
        if(targetWeuiGrid.parentNode.id=="originMicroservice"){
            $(this).appendTo("#allMicroservice");
        }else{
            $(this).appendTo("#originMicroservice");
        }
    })
}