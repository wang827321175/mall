/**
 * Created by shenbiao on 2017/3/5.
 * 公共js
 */

//判断是什么浏览器

var browser={
    versions:function(){
        var u = navigator.userAgent, app = navigator.appVersion;
        return {//移动终端浏览器版本信息
            trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
            iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
            weixin: u.indexOf('MicroMessenger') > -1, //是否微信
            qq: u.match(/\sQQ/i) == " qq" //是否QQ
        };
    }(),
    language:(navigator.browserLanguage || navigator.language).toLowerCase()
}

$(function () {
    //对话框点击消失按钮
    $('#dialogs').on('click', '.weui-dialog__btn', function () {
        $(this).parents('.js_dialog').fadeOut(200);
    });

    //上方确认按钮
    $('.weui-navbar__item').on('click', function () {
        $(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
    });
});


function showActionDialog(title,content,confirmFunction){
    $("#iosDialog1_title").text(title);
    $("#iosDialog1_content").text(content);
    $('#iosDialog1_myfunction').off('click');
    $("#iosDialog1_myfunction").click(confirmFunction);
    $("#tpl_dialogs").css("display","block");
    $('#iosDialog1').fadeIn(200);
}

function showIosDialogInput(title,placeholder,confirmFunction){
    $("#iosDialog_input_title").text(title);
    $("#iosDialog_input_content").attr('placeholder',placeholder);
    $('#iosDialog_input_myfunction').off('click');
    $("#iosDialog_input_myfunction").click(confirmFunction);
    $("#tpl_dialogs").css("display","block");
    $('#iosDialog_input').fadeIn(200);
}

function showInnerContent() {
    //点击显示子内容
    var winH = $(window).height();
    var categorySpace = 10;

    $('.js_category').on('click', function () {
        var $this = $(this),
            $inner = $this.next('.js_categoryInner'),
            $page = $this.parents('.page'),
            $parent = $(this).parent('li');
        var innerH = $inner.data('height');
        bear = $page;

        if (!innerH) {
            $inner.css('height', 'auto');
            innerH = $inner.height();
            $inner.removeAttr('style');
            $inner.data('height', innerH);
        }

        if ($parent.hasClass('js_show')) {
            $parent.removeClass('js_show');
        } else {
            $parent.siblings().removeClass('js_show');

            $parent.addClass('js_show');
            if (this.offsetTop + this.offsetHeight + innerH > $page.scrollTop() + winH) {
                var scrollTop = this.offsetTop + this.offsetHeight + innerH - winH + categorySpace;

                if (scrollTop > this.offsetTop) {
                    scrollTop = this.offsetTop - categorySpace;
                }

                $page.scrollTop(scrollTop);
            }
        }
    });
}


function searchBarAction() {
    var $searchBar = $('#searchBar'),
        $searchResult = $('#searchResult'),
        $searchText = $('#searchText'),
        $searchInput = $('#searchInput'),
        $searchClear = $('#searchClear'),
        $searchCancel = $('#searchCancel');

    function hideSearchResult() {
        $searchResult.hide();
        $searchInput.val('');
    }

    function cancelSearch() {
        hideSearchResult();
        $searchBar.removeClass('weui-search-bar_focusing');
        $searchText.show();
    }

    $searchText.on('click', function () {
        $searchBar.addClass('weui-search-bar_focusing');
        $searchInput.focus();
    });
    $searchInput
        .on('blur', function () {
            if (!this.value.length) cancelSearch();
        })
        .on('input', function () {
            if (this.value.length) {
                $searchResult.show();
            } else {
                $searchResult.hide();
            }
        })
    ;
    $searchClear.on('click', function () {
        hideSearchResult();
        $searchInput.focus();
    });
    $searchCancel.on('click', function () {
        cancelSearch();
        $searchInput.blur();
    });
}
