$(function() {
	var listUrl = '/gradudes/superadmin/getuseshoplist'
	var statusUrl = '/gradudes/superadmin/modifyshopsuper';
	function getList() {
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var shopList = data.shopList;
				var tempHtml = '';
				shopList.map(function(item, index) {
					
					tempHtml += '' + '<div class="row row-shop">'
							+ '<div class="col-25">'
							+ item.shopName
							+ '</div>'
							+ '<div class="col-20">'
							+ item.owner.name
							+ '</div>'
							+ '<div class="col-40">'
							+ item.phone
							+ '</div>'
							+ '<div class="col-30">'							
							+ '<a href="#" class="edit" data-id="'
							+ item.shopId
							+ '"data-status="'
							+ item.enableStatus
							+'">审核</a>'
							+ '</div>'
							+ '</div>';
				});
				$('.shop-wrap').html(tempHtml);
			}
		});
	}

	getList();

	function changeItemStatus(id,enableStatus) {
		var shop = {};
		shop.shopId = id;
		shop.enableStatus = enableStatus;
		$.confirm('确定吗?', function() {
			$.ajax({
				url : statusUrl,
				type : 'POST',
				data : {
					shopStr : JSON.stringify(shop),
					statusChange : true
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('操作成功！');
						getList();
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}

	$('.shop-wrap')
			.on(
					'click',
					'a',
					function(e) {
						var target = $(e.currentTarget);
						  if (target.hasClass('edit')) {
								changeItemStatus(e.currentTarget.dataset.id,e.currentTarget.dataset.status);
							}
					});

	
});