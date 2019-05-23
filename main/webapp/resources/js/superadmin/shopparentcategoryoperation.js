$(function() {

	//获取shopCategoryId的参数值
	var shopCategoryId = getQueryString('shopCategoryId');
     //通过shopCategoryId获取头条信息的URL
	var infoUrl = '/gradudes/superadmin/getshopcategorybyid?shopCategoryId=' + shopCategoryId;
	//
	var categoryUrl='';
	//更新父类别信息的URL
	var shopCategoryPostUrl = '/gradudes/superadmin/modifyshopcategory';
	//由于店铺类别添加合编辑使用的是同一个页面
	//该标识符用来标明是添加还是编辑操作
	var isEdit = false;
	if (shopCategoryId) {
		//若有shopCategoryId则为编辑操作
		getInfo(shopCategoryId);
		isEdit = true;
	} else {
		getCategory();
		shopCategoryPostUrl = '/gradudes/superadmin/addshopcategory';
	}
	//获取需要编辑的商店类别的商店类别信息，并赋值给表单
	function getInfo(id) {
		$
				.getJSON(
						infoUrl,
						function(data) {
							if (data.success) {
								//从返回的JSON当中获取shopCateGory对象的信息，并赋值给表单
								var shopCategory = data.shopCategory;
								$('#shop-category-name').val(shopCategory.shopCategoryName);
								$('#shop-category-desc').val(shopCategory.shopCategoryDesc);
								$('#priority').val(shopCategory.priority);
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
				var shopCategory = {};
				shopCategory.shopCategoryName = $('#shop-category-name').val();
				shopCategory.priority = $('#priority').val();
				shopCategory.shopCategoryDesc =$('#shop-category-desc').val();
				
				shopCategory.shopCategoryId=shopCategoryId;
               //获取缩略图文件流
				var shopCategoryImg = $('#small-img')[0].files[0];
				//生成表单对象，用于接受参数并传递给后台
				var formData = new FormData();
				formData.append('shopCategoryImg', shopCategoryImg);
				//将product json对象转成字符流保存至表单对象key 为productStr的键值对里
				formData.append('shopCategoryStr', JSON.stringify(shopCategory));
				//获取表单的验证码
				var verifyCodeActual = $('#j_captcha').val();
				if (!verifyCodeActual) {
					$.toast('请输入验证码！');
					return;
				}
				formData.append("verifyCodeActual", verifyCodeActual);
				//将数据提交给后台处理相关操作
				$.ajax({
					url : shopCategoryPostUrl,
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
							$.toast('提交失败');
							$('#captcha_img').click();
						}
					}
				});
			});

});