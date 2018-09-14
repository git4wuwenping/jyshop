//发货
$("#deliveryBtn").click(function(){
	var order_id=$("[name='id']").val();
	if(typeof order_id=='undefined' || order_id=='')
		$.Mod.Close("获取订单ID失败","error");
	showModal('订单发货', '/admin/bargain/order/delivery/'+order_id, 460);
});


$("#cancleBtn").click(function(){

	var order_id=$("[name='id']").val();
	if(typeof order_id=='undefined' || order_id=='')
		$.Mod.Close("获取订单ID失败","error");
	var url=_urlPath+"admin/bargain/order/cancleOrder/"+order_id;
	$.ajax({
        url: url,
        type: "post",
        dataType:"json",
        success: function (req){
            if (req.retcode == 0) {
                $.Mod.Close("订单己作废...");
                goPage("bargain/order/main");
            } else {
            	$.Mod.Close(req.retmsg,"error");
            }
        }
    });
});
