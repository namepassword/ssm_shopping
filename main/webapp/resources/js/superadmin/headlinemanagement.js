$(function() {
	var listUrl = '/gradudes/superadmin/listheadlines'
	var statusUrl = '/gradudes/superadmin/deleteheadlinebyid';
	function getList() {
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var headLineList = data.headLineList;
				var tempHtml = '';
				headLineList.map(function(item, index) {
					
					tempHtml += '' + '<div class="row row-headline">'
							+ '<div class="col-33">'
							+ item.lineName
							+ '</div>'
							+ '<div class="col-20">'
							+ item.priority
							+ '</div>'
							+ '<div class="col-40">'
							+ '<a href="#"  class="edit" data-id="'
							+ item.lineId
							+ '" data-status="'
							+ item.enableStatus
							+ '">编辑</a>'
							+ '<a href="#" class="delete" data-id="'
							+ item.lineId
							+'">删除</a>'
							+ '</div>'
							+ '</div>';
				});
				$('.headline-wrap').html(tempHtml);
			}
		});
	}

	getList();

	function changeItemStatus(id) {
		var headline = {};
		headline.lineId = id;
		$.confirm('确定吗?', function() {
			$.ajax({
				url : statusUrl,
				type : 'POST',
				data : {
					headLineStr : JSON.stringify(headline),
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

	$('.headline-wrap')
			.on(
					'click',
					'a',
					function(e) {
						var target = $(e.currentTarget);
						if (target.hasClass('edit')) {
							window.location.href = '/gradudes/superadmin/headlineoperation?lineId='
									+ e.currentTarget.dataset.id;
						} else if (target.hasClass('delete')) {
							changeItemStatus(e.currentTarget.dataset.id,
									e.currentTarget.dataset.status);
						}
					});

	
});