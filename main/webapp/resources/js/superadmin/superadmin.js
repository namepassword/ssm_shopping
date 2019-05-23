$(function () {
	getlist();
	
	
	
	function getlist(e) {
		$.ajax({
			url : "/gradudes/superadmin/getsuperadminname",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					
					handleUser(data.user);
				}
			}
		});
	}

	
	function handleUser(data) {
		$('#user-name').text(data.name);
	}



});
