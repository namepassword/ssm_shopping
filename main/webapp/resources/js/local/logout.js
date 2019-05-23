$(function(){
	
	$('#log-out').click(function(){

	//清除session
		$.ajax({
			url:'/gradudes/local/loginout',
			type:"post",
			async:false,
			cache:false,
			dataType:'json',
			success:function(data){
				if(data.success){
					var usertype=$('#log-out').attr("usertype");
					//出成功后退出到登录页面
					window.location.href="/gradudes/local/login?usertype="+usertype;
					return false;
				}
			},
			error:function(data,error){
				alert(error);
			}
			
		});
		
		
	
	});
	
});