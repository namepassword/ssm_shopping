$(function() {
	var shopAuthId = getQueryString('shopAuthId');
      //根据主键获取授权信息的URL
	var infoUrl = '/gradudes/shopadmin/getshopauthmapbyid?shopAuthId=' + shopAuthId;
     //修改授权信息的URL
	var shopAuthPostUrl = '/gradudes/shopadmin/modifyshopauthmap';

	if (shopAuthId) {
		getInfo(shopAuthId);
	} else {
		$.toast('用户不存在！');
	window.location.href = '/gradudes/shopadmin/shopmanagement';
	}

	function getInfo(id) {
		$.getJSON(infoUrl, function(data) {
			if (data.success) {
				var shopAuthMap = data.shopAuthMap;
				$('#shopauth-name').val(shopAuthMap.employee.name);
				$('#title').val(shopAuthMap.title);
			}
		});
	}

	$('#submit').click(function() {
		var shopAuth = {};
		var employee={};
		//获取要修改的信息并传入后台处理
		shopAuth.employee=employee;
		shopAuth.employee.name=$('shopauth-name').val();
		shopAuth.title = $('#title').val();
		shopAuth.shopAuthId = shopAuthId;
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		$.ajax({
			url : shopAuthPostUrl,
			type : 'POST',
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			data : {
				shopAuthMapStr : JSON.stringify(shopAuth),
				verifyCodeActual : verifyCodeActual
			},
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