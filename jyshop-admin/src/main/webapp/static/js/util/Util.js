var pageUrls=[];


//填充页面
function goPage(pageUrl) {
	pageUrls.push(pageUrl)

	if(_urlPath!="/")
	    pageUrl=_urlPath+pageUrl;
	
	
	$.ajax({
		url : pageUrl,
		type : "post",
		dataType : 'html',
		success : function(req) {
			$("#content-wrapper").html(req);
		},
		error : function(req) {
			console.log(req);
			$("#content-wrapper").html(req.responseText);
//			if (404 == req.status) {
//				console.log(_urlPath+ "/404");
//				goPage(_urlPath + "/404");
//			} else {
//				console.log(_urlPath+ "/500");
//				goPage(_urlPath + "/500");
//			}
		}
	});
}
function backgo(){
	
	var page;
	if(pageUrls.length>1){
		if(pageUrls.length>0){
		page=pageUrls.splice(pageUrls.length-2,1)
	}
	$.ajax({
		url : page,
		type : "post",
		dataType : 'html',
		success : function(req) {
			$("#content-wrapper").html(req);
		},
		error : function(req) {
			console.log(req);
			$("#content-wrapper").html(req.responseText);
//			if (404 == req.status) {
//				console.log(_urlPath+ "/404");
//				goPage(_urlPath + "/404");
//			} else {
//				console.log(_urlPath+ "/500");
//				goPage(_urlPath + "/500");
//			}
		}
	});
	}
	
	
	
}


function goPageMenu(pageUrl,opId) {
			
			
 			if(pageUrl!="#" && pageUrl!=""){
				goPage(pageUrl)
				return;
			}
		
			pageUrl="/admin/menuAjax";
				
			if(_urlPath!="/")
			    pageUrl=_urlPath+pageUrl;
			
			$.ajax({
				url : pageUrl,
				type : "post",
				data: {opId: opId},
				dataType : 'html',
				success : function(req) {
					$("ul[class='navigation']").html(req);
					$("#content-wrapper").html("");
				},
				error : function(req) {
					console.log(req);
					$("#content-wrapper").html(req.responseText);
		//			if (404 == req.status) {
		//				console.log(_urlPath+ "/404");
		//				goPage(_urlPath + "/404");
		//			} else {
		//				console.log(_urlPath+ "/500");
		//				goPage(_urlPath + "/500");
		//			}
				}
			});
}

//弹出模态框
function showModal(title, url, width, height){
	if(undefined == height){
		height = "auto";
	}
	if(_urlPath!="/")
		url=_urlPath+url;
	$.Mod({
    	title: title,
    	url: url,
		width: width,
		height: height
	});
}

//关闭模态框
function closeModal() {
	$(".close", ".modal").click();
}

//弹出确认框
function showCfm(title, url){
	if(_urlPath!="/")
		url=_urlPath+url;
	$.Cfm(title,function(){
        $.ajax({
            url:  url,
            type:"post",
            dataType:"json",
            success: function (req){
                if (req.retcode == 0) {
                    $("#openAppGrid").sgrid("refresh");
                    $.Suc(req.retmsg);
                } else {
                    $.Err(req.retmsg);
                }
            }
        });
    });
}

//表单ajax请求
function ajaxRequest(url, data){
	if(_urlPath!="/")
		url=_urlPath+url;
	$.ajax({
        url: url,
        data: data,
        type: "post",
        dataType:"json",
        success: function (req){
            if (req.retcode == 0) {
                $("#openAppGrid").sgrid("refresh");
                $.Mod.Close(req.retmsg);
            } else {
                modalErr(req.retmsg);
            }
        }
    });
}

//表格ajax请求
function ajaxGrid(url, data){
	if(_urlPath!="/")
		url=_urlPath+url;
	$.ajax({
		url:  url,
		data: data,
		type: "post",
		dataType:"json",
		success: function (req){
			if (req.retcode == 0) {
				$("#openAppGrid").sgrid("refresh");
				//$.Suc(req.retmsg);
				$.Mod.Close(req.retmsg);
			} else {
				$.Err(req.retmsg);
			}
		}
	});
}

//表格ajax请求
function postGrid(url, data){
	if(_urlPath!="/")
		url=_urlPath+url;
	$.ajax({
		url: url,
		data: data,
		type: "post",
		dataType:"json",
		success: function (req){
			if (req.retcode == 0) {
				$("#openAppGrid").sgrid("searchLoad");
				//$.Suc(req.retmsg);
				$.Mod.Close(req.retmsg);
			} else {
				$.Err(req.retmsg);
			}
		}
	});
}

function modalResult(req){
	if (req.retcode == 0) {
        $("#openAppGrid").sgrid("refresh");
        $.Mod.Close(req.retmsg);
    } else {
        modalErr(req.retmsg);
    }
}

//开始计时
function event_timer(time_remain) {
	if(time_remain > 0){
		rt = parseInt(time_remain);
		var eventid = document.getElementById('event_time_remain');
		eventid.innerHTML = time_format(rt);
		time_remain = time_remain - 1;
		setTimeout("event_timer('" + time_remain + "')", 1000);
	}
}

//以hh:mm:ss格式化时间，可以根据需要 定义格式
function time_format(s) {
	var t;
	if (s > -1) {
		hour = Math.floor(s / 3600);
		min = Math.floor(s / 60) % 60;
		sec = s % 60;
		day = parseInt(hour / 24);
		if (day > 0) {
			hour = hour - 24 * day;
			t = day + "天" + hour + "时";
		} else
			t = hour + "时";

		if (min < 10) {
			t += "0";
		}
		t += min + "钟";
		if (sec < 10) {
			t += "0";
		}
		t += sec + "秒";
	} else {
		t = "0天0时0分0秒";
	}
	return t;
}

//焦点失去事件
function onblurEvent(type){
	var total = getTotal();
	$("#total").val(total);
	if(1 == type){
		var oddEven = getOddEven(total);
		$("#oddEven").val(oddEven);
	}else if(2 == type){
		var oddEven = getOddEven(total);
		$("#oddEven").val(oddEven);
		var tailSize = getTailSize(total);
		$("#tailSize").val(tailSize);
	}else if(3 == type){
		var oddEven = getOddEven(total);
		$("#oddEven").val(oddEven);
		var fiveElements = getFiveElements(total);
		$("#fiveElements").val(fiveElements);
	}
}

//初始化百度富文本编辑器
function initUeditor(){
	UE.delEditor("content");
	UE.getEditor("content",{
		serverUrl: "/upload/ueditoUpload",
		autoClearinitialContent: false,
		retainOnlyLabelPasted: true,
	    toolbars:[[
			"fullscreen", "source", "|", "undo", "redo", "|",
			"bold", "italic", "underline", "fontborder", "strikethrough", "superscript", "subscript", "removeformat", "formatmatch", "autotypeset", "blockquote", "pasteplain", "|", "forecolor", "backcolor", "insertorderedlist", "insertunorderedlist", "selectall", "cleardoc", "|",
			"rowspacingtop", "rowspacingbottom", "lineheight", "|",
			"customstyle", "paragraph", "fontfamily", "fontsize", "|",
			"directionalityltr", "directionalityrtl", "indent", "|",
			"justifyleft", "justifycenter", "justifyright", "justifyjustify", "|", "touppercase", "tolowercase", "|",
			"link", "unlink", "anchor", "|", "imagenone", "imageleft", "imageright", "imagecenter", "|",
			"simpleupload", "insertimage", "emotion", "scrawl", "insertvideo", "music", "attachment", "map", "gmap", "insertframe", "insertcode", "webapp", "pagebreak", "template", "background", "|",
			"horizontal", "date", "time", "spechars", "snapscreen", "wordimage", "|",
			"inserttable", "deletetable", "insertparagraphbeforetable", "insertrow", "deleterow", "insertcol", "deletecol", "mergecells", "mergeright", "mergedown", "splittocells", "splittorows", "splittocols", "charts", "|",
			"print", "preview", "searchreplace", "drafts", "help"           
	    ]],
	    //默认的编辑区域高度
	    initialFrameHeight:300
	    //更多其他参数，请参考ueditor.config.js中的配置项
	});
}

//初始化百度富文本编辑器
function initBaidiUeditor(editorName){
    UE.delEditor(editorName);
    UE.getEditor(editorName,{
        serverUrl: "/upload/ueditoUpload",
        autoClearinitialContent: false,
        retainOnlyLabelPasted: true,
        toolbars:[[
            "fullscreen", "source", "|", "undo", "redo", "|",
            "bold", "italic", "underline", "fontborder", "strikethrough", "superscript", "subscript", "removeformat", "formatmatch", "autotypeset", "blockquote", "pasteplain", "|", "forecolor", "backcolor", "insertorderedlist", "insertunorderedlist", "selectall", "cleardoc", "|",
            "rowspacingtop", "rowspacingbottom", "lineheight", "|",
            "customstyle", "paragraph", "fontfamily", "fontsize", "|",
            "directionalityltr", "directionalityrtl", "indent", "|",
            "justifyleft", "justifycenter", "justifyright", "justifyjustify", "|", "touppercase", "tolowercase", "|",
            "link", "unlink", "anchor", "|", "imagenone", "imageleft", "imageright", "imagecenter", "|",
            "simpleupload", "insertimage", "emotion", "scrawl", "insertvideo", "music", "attachment", "map", "gmap", "insertframe", "insertcode", "webapp", "pagebreak", "template", "background", "|",
            "horizontal", "date", "time", "spechars", "snapscreen", "wordimage", "|",
            "inserttable", "deletetable", "insertparagraphbeforetable", "insertrow", "deleterow", "insertcol", "deletecol", "mergecells", "mergeright", "mergedown", "splittocells", "splittorows", "splittocols", "charts", "|",
            "print", "preview", "searchreplace", "drafts", "help"
        ]],
        //默认的编辑区域高度
        initialFrameHeight:300
        //更多其他参数，请参考ueditor.config.js中的配置项
    });
}

//回显旧图片,再上传新图片
function uploadImg(btnId, valueId, url, imgs) {
	if("" != imgs){
		var images = "";
		var imgArr = imgs.split(",");
		for (var i in imgArr) {
			images += "<img src='" + imgArr[i] + "' class='file-preview-image' style='width:100px;height:100px;'>,"
		}
		if (images.length > 0) {
			images = images.substring(0, images.length - 1);
		}
		preImg(btnId, valueId, url, images);
	}else{
		$("#" + btnId).fileinput({
			enctype : "multipart/form-data",
			uploadUrl : _urlPath + url,
			language : "zh",
			browseClass : "btn btn-info",
			browseLabel : "选择图片",
			maxFileCount : 10,
			showUpload : true, //是否显示上传按钮
			showRemove : true, //是否显示移除按钮
			showCaption : false, //是否显示标题
			dropZoneEnabled : false, //是否显示预览区域
			allowedFileExtensions : [ "jpg", "png", "gif" ] //接收的文件后缀
		}).on("fileuploaded", function(event, ret, previewId, index) {
			var filePath = ret.response.data.filepath;
			$("#" + valueId).val(filePath);
		}).on("fileclear", function(event, key) {
			return false;
		}).on("filecleared", function(event, key) {
			$("#" + valueId).val("");
			return false;
		}).on("change", function(event, key) {
			return false;
		});
	}
}

//回显图片
function preImg(btnId, valueId, url, images){
	$("#" + btnId).fileinput({
		enctype : "multipart/form-data",
		uploadUrl : _urlPath + url,
		language : "zh",
		browseClass : "btn btn-info",
		browseLabel : "选择图片",
		maxFileCount : 10,
		showUpload : true, //是否显示上传按钮
		showRemove : true, //是否显示移除按钮
		showCaption : false, //是否显示标题
		dropZoneEnabled : false, //是否显示预览区域
		allowedFileExtensions : [ "jpg", "png", "gif" ], //接收的文件后缀
		initialPreview: [images]
	}).on("fileuploaded", function(event, ret, previewId, index) {
		var filePath = ret.response.data.filepath;
		$("#" + valueId).val(filePath);
	}).on("fileclear", function(event, key) {
		return false;
	}).on("filecleared", function(event, key) {
		$("#" + valueId).val("");
		return false;
	}).on("change", function(event, key) {
		return false;
	});
}