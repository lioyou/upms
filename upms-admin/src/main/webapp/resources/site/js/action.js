/**
 * 处理create update delete 
 */

//create
/**
 * 创建操作
 * @param uri 资源标识
 * @param 对话框名称
 * @Param 文件上传input的Id，可以不传
 * 
 */
function createAction(uri,dilogTitle,fileId) {
	dialog = $.dialog({
		animationSpeed: 300,
		title: dilogTitle,
		content: 'url:' + basePath + uri,
		onContentReady: function () {
			initMaterialInput();
			if(fileId){
				initUploader(basePath,fileId);
			}
		}
	});
}




//update
/**
 * 更新操作
 * @param uri 资源标识
 * @param 对话框名称
 * @param 表格中id的名称 如userId,systemId
 * @Param 文件上传input的Id
 * 
 */
function updateAction(uri,dialogTitle,id,fileId) {
	var rows = $table.bootstrapTable('getSelections');
	if (rows.length != 1) {
		$.confirm({
			title: false,
			content: '请选择一条记录！',
			autoClose: 'cancel|3000',
			backgroundDismiss: true,
			buttons: {
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	} else {
		dialog = $.dialog({
			animationSpeed: 300,
			title: dialogTitle,
			content: 'url:' + basePath + uri + rows[0][id],
			onContentReady: function () {
				initMaterialInput();
				if(fileId){
					initUploader(basePath,fileId);
				}
			}
		});
	}
}


//delete
/**
 * 删除操作
 * @param uri 资源标识
 * @param 对话框名称
 * @param 表格中id的名称 如userId,systemId
 * 
 */
function deleteAction(uri,dialogTitle,id) {
	var rows = $table.bootstrapTable('getSelections');
	if (rows.length == 0) {
		$.confirm({
			title: false,
			content: '请至少选择一条记录！',
			autoClose: 'cancel|3000',
			backgroundDismiss: true,
			buttons: {
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	} else {
		dialog = $.confirm({
			type: 'red',
			animationSpeed: 300,
			title: dialogTitle,
			content: '确认删除吗？',
			buttons: {
				confirm: {
					text: '确认',
					btnClass: 'waves-effect waves-button',
					action: function () {
						var ids = new Array();
						for (var i in rows) {
							ids.push(rows[i][id]);
						}
						$.ajax({
							type: 'get',
							url:basePath + uri  + ids.join("-"),
							success: function(result) {
								if (result.code != 1) {
									if (result.data instanceof Array) {
										$.each(result.data, function(index, value) {
											$.confirm({
												theme: 'dark',
												animation: 'rotateX',
												closeAnimation: 'rotateX',
												title: false,
												content: value.errorMsg,
												buttons: {
													confirm: {
														text: '确认',
														btnClass: 'waves-effect waves-button waves-light'
													}
												}
											});
										});
									} else {
										$.confirm({
											theme: 'dark',
											animation: 'rotateX',
											closeAnimation: 'rotateX',
											title: false,
											content: result.data.errorMsg,
											buttons: {
												confirm: {
													text: '确认',
													btnClass: 'waves-effect waves-button waves-light'
												}
											}
										});
									}
								} else {
									dialog.close();
									$table.bootstrapTable('refresh');
								}
							},
							error: function(XMLHttpRequest, textStatus, errorThrown) {
								$.confirm({
									theme: 'dark',
									animation: 'rotateX',
									closeAnimation: 'rotateX',
									title: false,
									content: textStatus,
									buttons: {
										confirm: {
											text: '确认',
											btnClass: 'waves-effect waves-button waves-light'
										}
									}
								});
							}
						});
					}
				},
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	}
}