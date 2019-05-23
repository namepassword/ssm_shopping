$(function(){
   //修改平台密码的controller url
	var url='/gradudes/local/changelocalpwd';
	//从地址栏的url里获取usertype
	//usertype=1则为customer，其余为shopowner
	var usertype=getQueryString("usertype");
	$('#submit').click(function(){
		//获取输入的帐号
		var userName=$('#username').val();
		//获取原密码
	    var password=$('#password').val();
	    //获取新密码
		var newPassword=$('#newPassword').val();
		//获取再次确认密码
	    var confirmPassword=$('#confirmPassword').val();
	    if(newPassword!=confirmPassword){
	    	$.toast("两次输入的新密码不一样！");
	    	return;
	    }
	    //添加表单数据
	    var formData=new FormData();
	    formData.append('userName',userName);
	    formData.append('password',password);
	    formData.append('newPassword',newPassword);
	  //获取验证码信息
	    var verifyCodeActual=$('#j_captcha').val();
	    if(!verifyCodeActual){
    		$.toast("请输入验证码!");
    		return;
    	}
	    formData.append('verifyCodeActual',verifyCodeActual);
	    //将参数post到后台去修改密码
	    $.ajax({
	    	url:url,
	    	type:"post",
	    	data:formData,
	    	contentType:false,
	    	processData:false,
	    	cacha:false,
	    	success:function(data){
	    		if(data.success){
	    			$.toast("提交成功!");
	    			if(usertype==1){
	    				//如果用户在前端展示系统页面则自动链接到前端展示系统
	    				window.location.href="/gradudes/frontend/index";
	    			}if(usertype==2){
	    				window.location.href="/gradudes/shopadmin/shoplist";
	    			}if(usertype==3){
	    				window.location.href="/gradudes/superadmin/superadminmanagement";
	    			}
	    		}else{
	    			$.toast("提交失败!"+data.errMsg);
	    			$("#captcha_img").click();
	    		}
	    	}
	    });
	});
	$('#back').click(function(){
		window.location.href="/gradudes/local/login?usertype="+usertype;
	});

});