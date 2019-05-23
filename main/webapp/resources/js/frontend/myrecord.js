$(function() {
	var loading = false;
	var maxItems = 20;
	var pageSize = 10;

	var listUrl = '/gradudes/frontend/listuserproductmapsbycustomer';

	var pageNum = 1;
	var productName = '';
	addItems(pageSize, pageNum);
	function addItems(pageSize, pageIndex) {
		// 生成新条目的HTML
		var url = listUrl + '?pageIndex=' + pageIndex
				+ '&pageSize=' + pageSize + '&productName=' + productName;
		loading = true;
		$.getJSON(url, function(data) {
			if (data.success) {
				maxItems = data.count;
				var html = '';
				data.userProductMapList.map(function(item, index) {
					html += '' + '<div class="card" data-product-id='
							+ item.productId + '>'
							+ '<div class="card-header">' + item.shop.shopName
							+ '</div>' + '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.product.productName
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.createTime).Format("yyyy-MM-dd")
							+ '</p>' + '<span>积分：' + item.point + '</span>'
							+ '</div>' + '</div>';
				});
				$('.list-div').append(html);
				var total = $('.list-div .card').length;
				if (total >= maxItems) {
					// 隐藏提示符
					$('.infinite-scroll-preloader').hide();
				}else{
					$('.infinite-scroll-preloader').show();
				}
				pageNum += 1;
				loading = false;
				$.refreshScroller();
			}
		});
	}

	

	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});

	$('#search').on('input', function(e) {
		productName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});
	$.init();
});
