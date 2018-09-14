//发货
$("#deliveryBtn").click(function(){
	var order_id=$(this).attr("order_id");
	if(typeof order_id=='undefined' || order_id=='')
		$.Mod.Close("获取订单ID失败","error");
	showModal('订单发货', '/admin/giftorder/preOrderDelivery/'+order_id, 460);
});


$("#cancleBtn").click(function(){

	var order_id=$(this).attr("order_id");
	if(typeof order_id=='undefined' || order_id=='')
		$.Mod.Close("获取订单ID失败","error");
	var url=_urlPath+"admin/giftorder/cancleOrder/"+order_id;
	$.ajax({
        url: url,
        type: "post",
        dataType:"json",
        success: function (req){
            if (req.retcode == 0) {
                $.Mod.Close("订单己作废...");
                //goPage("admin/order/queryOrderDetail/"+order_id);
                goPage("order/orderMain");
            } else {
            	$.Mod.Close(req.retmsg,"error");
            }
        }
    });
});
