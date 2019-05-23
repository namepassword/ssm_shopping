$(function() {

	//获取lineId的参数值
	var lineId = getQueryString('lineId');
     //通过lineId获取头条信息的URL
	var infoUrl = '/gradudes/superadmin/getheadlinebyid?lineId=' + lineId;
	//获取当前店铺设定的商品类别列表
	var categoryUrl = '/gradudes/frontend/listmainpageinfo';
	//更新头条信息的URL
	var headLinePostUrl = '/gradudes/superadmin/modifyheadline';
	//由于商品添加合编辑使用的是同一个页面
	//该标识符用来标明是添加还是编辑操作
	var isEdit = false;
	if (lineId) {
		//若有lineId则为编辑操作
		getInfo(lineId);
		isEdit = true;
	} else {
		getCategory();
		headLinePostUrl = '/gradudes/superadmin/addheadline';
	}
//获取需要编辑的商品的商品信息，并赋值给表单
	function getInfo(id) {
		$
				.getJSON(
						infoUrl,
						function(data) {
							if (data.success) {
								//从返回的JSON当中获取headline对象的信息，并赋值给表单
								var headline = data.headLine;
								$('#line-name').val(headline.lineName);
								$('#priority').val(headline.priority);
								$('#line-link').val(headline.lineLink);
							}
						});
	}

	function getCategory() {
		$.getJSON(categoryUrl, function(data) {
			if (data.success) {
				var shopCategoryList = data.shopCategoryList;
				var optionHtml = '';
				shopCategoryList.map(function(item, index) {
					optionHtml += '<option data-value="'
							+ item.shopCategoryId + '">'
							+ item.shopCategoryName + '</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}

	
//提交按钮的时间响应，分别对头条的添加和编辑操作做不同响应
	$('#submit').click(
			function() {
				//创建头条json对象 ，并从表单里面获取对应的属性值
				var headline = {};
				headline.lineName = $('#line-name').val();
				headline.priority = $('#priority').val();
				//获取选定的头条连接类别值
				headline.lineLink =$('#line-link').val();
				
				headline.lineId=lineId;
               //获取缩略图文件流
				var headLineImg = $('#small-img')[0].files[0];
				//生成表单对象，用于接受参数并传递给后台
				var formData = new FormData();
				formData.append('headLineImg', headLineImg);
				//将product json对象转成字符流保存至表单对象key 为productStr的键值对里
				formData.append('headLineStr', JSON.stringify(headline));
				//获取表单的验证码
				var verifyCodeActual = $('#j_captcha').val();
				if (!verifyCodeActual) {
					$.toast('请输入验证码！');
					return;
				}
				formData.append("verifyCodeActual", verifyCodeActual);
				//将数据提交给后台处理相关操作
				$.ajax({
					url : headLinePostUrl,
					type : 'POST',
					data : formData,
					contentType : false,
					processData : false,
					cache : false,
					success : function(data) {
						if (data.success) {
							$.toast('提交成功！');
							$('#captcha_img').click();
						} else {
							$.toast('提交失败！');
							$('#captcha_img').click();
						}
					}
				});
			});

});