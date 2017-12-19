/**
 * 文件上传
 */

/**
 * bashPath 根目录名称,如 /upms-amdin
 * fileId input的Id名称，用于存放文件名称的,如 #idName
 * 
 */
function initUploader(basePath,fileId) {
	//百度上传按钮
	var uploader = WebUploader.create({
		// swf文件路径
		swf: basePath + '/resources/denpendences/plugins/webuploader-0.1.5/Uploader.swf',
		// 文件接收服务地址
		server: basePath + '/manage/image/upload',
		method: 'POST',
		// 选择文件的按钮。可选。
		// 内部根据当前运行是创建，可能是input元素，也可能是flash.
		pick: {
			"id": '#picker',
			"multiple": false
		},
		// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
		resize: false,
		// 选完文件后，是否自动上传。
		auto: false,
		// 只允许选择图片文件
		accept: {
			title: '图片文件',
			extensions: 'gif,jpg,jpeg,bmp,png',
			mimeTypes: 'image/git,image/jpg,image/jpeg,image/bmp,image/png'
		}
	});
	uploader.on( 'fileQueued', function(file) {
		//目标：展示缩略图
		//1.接收图片成功后，使用确认框来展示
		//2.按确定就直接上传
	    uploader.makeThumb(file,function(error,src){
	    	//有错误
	    	if(error){
	    		$.confirm({
	    			title: '错误信息',
	    			content: '图片错误，无法预览！',
	    			buttons: {
	    				confirm: {
	    					text: '确认',
	    					btnClass: 'waves-effect waves-button',
	    				},
	    			},
	    		});
	    		return ;
	    	}
	    	$.confirm({
	    		title: '请确认上传！',
	    		content: '<img src="' + src + '"/>',
	    		buttons: {
	    			confirm: {
	    				text: '确认',
		    			btnClass: 'waves-effect waves-button',
		    			action: function(){
		    				uploader.upload();
		    			},
	    			},
	    			cancel: {
	    				text: '取消',
	    				btnClass: 'waves-effect waves-button',
	    				action: function(){
	    					//重置队列，防止用户又想上传同一张图片，而无法显示
		    				uploader.reset();
	    				}
	    			},
	    		},
	    	});
	    },200,200);
	});
	//上传成功的回调函数
	uploader.on( 'uploadSuccess', function(file, result) {
		if(result.code == 1){
			$('#image-prefix').val(result.data);
			$.confirm({
				title: '通知消息',
				content: '上传成功',
				buttons: {
					confirm: {
						text: '确认',
						btnClass: 'btn btn-info',
					}
				}
			});
			//给头像字段自动赋值
			$(fileId).val(file.name);
			$(fileId).focus();
		}else{//服务端处理图片失败
			$.confirm({
				title: '通知消息',
				content: '上传失败：' + result.message,
				theme: 'dark',
				buttons: {
					confirm: {
						text: '确认',
						btnClass: 'btn btn-error',
					}
				}
			});	
		}
	});
	uploader.on( 'uploadError', function(file) {
		console.log('uploadError', file);
	});
}