$(function() {
    //列出该店铺下所有授权信息的url
    var listUrl = '/gradudes/shopadmin/listshopauthmapsbyshop?pageIndex=1&pageSize=9999';
    //修改授权信息的url
    var deleteUrl = '/gradudes/shopadmin/modifyshopauthmap';
getList();
    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var shopauthList = data.shopAuthMapList;
                var tempHtml = '';
                shopauthList.map(function (item, index) {
                	var textOp="恢复";
                	var contraryStatus=0;
                	if(item.enableStatus==1){
                		//若状态值位1，表明权限生效，
                		textOp="删除";
                		contraryStatus=0;
                	}else{
                		contraryStatus=1;
                	}
                	
                    tempHtml += ''
                         +      '<div class="row row-shopauth">'
                         +          '<div class="col-40">'+ item.employee.name +'</div>'
                         
                    
                    
                    if(item.titleFlag!=0){
                    	//若不是店家本人的授权信息，则加入编辑以及改变状态等操作
                    	tempHtml +='<div class="col-20">'+ item.title +'</div>'
                               +'<div class="col-40">'
                               +'<a href="#" class="edit" data-employee-id="'
                               + item.employee.userId +'" data-auth-id="'
                               + item.shopAuthId +'">编辑</a>'
                               + '<a href="#" class="status" data-auth-id="'
                               + item.shopAuthId+'" data-status="'
                               + contraryStatus +'">'+textOp+'</a>'
                               +'</div>'
                    }else{
                    	//若为店家，且不允许操作
                    	tempHtml +='<div class="col-20">'+ item.title +'</div>'
                        +'<div class="col-40">'
                        +'<span>不可操作</span>'+'</div>'
                    }
                    tempHtml+='</div>'; 
                });
                $('.shopauth-wrap').html(tempHtml);
            }
        });
    }



    function changeStatus(id,status) {
    	var shopAuth={};
    	shopAuth.shopAuthId=id;
    	shopAuth.enableStatus=status;
        $.confirm('确定么?', function () {
            $.ajax({
                url: deleteUrl,
                type: 'POST',
                data: {
                   shopAuthMapStr:JSON.stringify(shopAuth),
                   statusChange:true,
                   },
                dataType: 'json',
                success: function (data) {
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

    $('.shopauth-wrap').on('click', 'a', function (e) {
        var target = $(e.currentTarget);
        if (target.hasClass('edit')) {
            window.location.href = '/gradudes/shopadmin/shopauthedit?shopAuthId=' 
            	+ e.currentTarget.dataset.authId;
        } else if (target.hasClass('status')) {
        	changeStatus(e.currentTarget.dataset.authId,e.currentTarget.dataset.status);
        }
    });

   
});