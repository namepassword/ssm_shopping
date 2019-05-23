$(function(){
	//绑定帐号的controller url
	var bindUrl='/gradudes/local/bindlocalauth';
	//从地址栏的url里获取userType
	//userType=1则为前端展示，其余为店家管理系统
	var usertype=getQueryString("usertype");
	$('#submit').click(function(){
	//获取输入的帐号
		var userName=$('#username').val();
		//获取输入的密码
		var password =$('#psw').val();
		//获取输入的验证码
		var verifyCodeActual=$('#j_captcha').val();
		var needVerify=false;
		if(!verifyCodeActual){
			$.toast('请输入验证码！');
			return;
		}
		//访问后台，绑定帐号
		$.ajax({
			url:bindUrl,
			async:false,
			cacha:false,
			type:"post",
			dataType:'json',
			data:{
				userName:userName,
				password:password,
				verifyCodeActual:verifyCodeActual
			},
			success:function(data){
				if(data.success){
					$.toast("绑定成功！");
					if(usertype==1){
					//若用户在前端展示系统页面则自动退回带前端系统首页
					window.location.href='/gradudes/frontend/index';
					}if(usertype==2){
						//若用户是在店家系统管理页面则自动退回到列表页中
						window.location.href='/gradudes/shopadmin/shoplist';
					}if(usertype==3){
	    				window.location.href="/gradudes/superadmin/superadminmanagement";
	    			}
				}else{
					$.toast("提交失败！"+data.errMsg);
					$('#captcha_img').click();
				}
				
			}
		});
		
	});
	
});