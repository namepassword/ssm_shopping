$(function(){
	//登录验证的controller url
	var loginUrl='/gradudes/local/logincheck';
	//从地址栏的url里获取usertype
	//usertype=1则为customer，其余为shopowner
	var usertype=getQueryString("usertype");
	//登录次数，累计登录三次失败之后自动弹出验证码要求输入
	var loginCount=0;
	$('#submit').click(function(){
		//获取输入的帐号
		var userName=$('#username').val();
		//获取输入的面=密码
	    var password=$('#psw').val();
		//获取验证码信息
	    var verifyCodeActual=$('#j_captcha').val();
		//是否需要验证验证码，默认为false
	    var needVerify=false;
	    //如果登录三次都失败
	    if(loginCount>=3){
	    	//那么就需要验证验证码
	    	if(!verifyCodeActual){
	    		$.toast("请输入验证码");
	    		return;
	    	}else{
	    		needVerify=true;
	    	}
	    }
		//访问后台进行登录验证
	    $.ajax({
	    	url:loginUrl,
	    	async:false,
	    	cache:false,
	    	type:"post",
	    	dataType:'json',
	    	data:{
	    		userName:userName,
	    		password:password,
	    		verifyCodeActual:verifyCodeActual,
	    		//是否需要做验证码校验
	    		needVerify:needVerify,
	    	},
	    	success:function(data){
					if(data.success){
						$.toast("登录成功！");
						if(usertype==1){
							//如果用户在前端展示系统页面则自动链接到前端展示系统
							window.location.href="/gradudes/frontend/index";
						}if(usertype==2){
							window.location.href="/gradudes/shopadmin/shoplist";
						}if(usertype==3){
							window.location.href="/gradudes/superadmin/superadminmanagement";
						}
					}else{
	    			$.toast("登录失败!"+data.errMsg);
	    			loginCount++;
	    			if(loginCount>=3){
	    				//登录失败三次，需要做验证码校验
	    				$('#verifyPart').show();
	    			}
	    		}
	    	}
	    });
		
	});
	
});