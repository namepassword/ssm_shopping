$(function() {
	//定义访问后天，获取头条列表以及一级类别列表的URL
    var url = '/gradudes/frontend/listmainpageinfo';

    $.getJSON(url, function (data) {
        if (data.success) {
        	//获取后台传递过来的头条列表
            var headLineList = data.headLineList;
            var swiperHtml = '';
          //遍历头条列表，并拼接处轮播图组
            headLineList.map(function (item, index) {
                swiperHtml += ''
                            + '<div class="swiper-slide img-wrap">'
                            +      '<img class="banner-img" src="'+getContextPath()+ item.lineImg +'" alt="'+ item.lineName +'">'
                            + '</div>';
            });
            //将轮播图给前台控件
            $('.swiper-wrapper').html(swiperHtml);
           //设定轮播转换时间为3秒
            $(".swiper-container").swiper({
                autoplay: 3000,
                //用户对轮播图进行操作是，是否自动停止autoplay
                autoplayDisableOnInteraction: false
            });
            //获取后台传递过来的大类列表
            var shopCategoryList = data.shopCategoryList;
            var categoryHtml = '';
           //遍历大类列表，拼接处两两一行的类别
            shopCategoryList.map(function (item, index) {
                categoryHtml += ''
                             +  '<div class="col-50 shop-classify" data-category='+ item.shopCategoryId +'>'
                             +      '<div class="word">'
                             +          '<p class="shop-title">'+ item.shopCategoryName +'</p>'
                             +          '<p class="shop-desc">'+ item.shopCategoryDesc +'</p>'
                             +      '</div>'
                             +      '<div class="shop-classify-img-warp">'
                             +          '<img class="shop-img" src="'+getContextPath()+item.shopCategoryImg+'">'
                             +      '</div>'
                             +  '</div>';
                console.log(item.shopCategoryImg);
            });
            //将拼接好的类别赋值给前端展示
            $('.row').html(categoryHtml);
        }
    });
//点击我显示侧栏
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });

    $('.row').on('click', '.shop-classify', function (e) {
        var shopCategoryId = e.currentTarget.dataset.category;
        var newUrl = '/gradudes/frontend/shoplist?parentId=' + shopCategoryId;
        window.location.href = newUrl;
    });

});
