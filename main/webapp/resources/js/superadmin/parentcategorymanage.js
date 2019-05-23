$(function(){
	
	var listUrl='/gradudes/superadmin/getparentcategorylist';
	var addUrl='/gradudes/superadmin/addparentcategory';
	var deleteUrl='/gradudes/superadmin/removecategory';
	getList();
	function getList(){
		$.getJSON(
				listUrl,
				function(data) {
					if (data.success) {
						var dataList = data.shopCategoryList;
						$('.category-wrap').html('');
						var tempHtml = '';
						dataList
								.map(function(item, index) {
									tempHtml += ''
											+ '<div class="row row-shop-category now">'
											+ '<div class="col-33 shop-category-name">'
											+ item.shopCategoryName
											+ '</div>'
											+ '<div class="col-33">'
											+ item.priority
											+ '</div>'
											
											+ '<div class="col-33">'
											+ '<a href="#"  class="edit" data-id="'
											+ item.shopCategoryId
											+ '">编辑</a>'
											+ '<a href="#" class="delete" data-id="'
											+ item.shopCategoryId
											+'">删除</a>'
											+ '</div>'
											+ '</div>';
								});
						$('.category-wrap').append(tempHtml);
					}
				});
	}
	
	
	$('.category-wrap').on('click', '.row-shop-category.now .delete',
			function(e) {
				var target = e.currentTarget;
				$.confirm('确定删除吗?', function() {
					$.ajax({
						url : deleteUrl,
						type : 'POST',
						data : {
							shopCategoryId : target.dataset.id,
							
						},
						dataType : 'json',
						success : function(data) {
							if (data.success) {
								$.toast('删除成功！');
								getList();
							} else {
								$.toast('店铺类别信息下有子类别 ! ');
							}
						}
					});
				});
			});
	
	

	$('.category-wrap')
	.on(
			'click',
			'a',
			function(e) {
				var target = $(e.currentTarget);
				if (target.hasClass('edit')) {
					window.location.href = '/gradudes/superadmin/shopparentcategoryoperation?shopCategoryId='
							+ e.currentTarget.dataset.id;
				} 
			});
});