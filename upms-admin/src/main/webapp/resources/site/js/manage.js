/*
 * 1.载入时，直接通过Ajax加载数据
 * 2.初始化操作，取第一个系统，创建节点，挂载节点
 * 3.监听操作，删除原先节点，再创建节点，挂载节点
 * 
 * 
 * 
 * 
 */

var systemData ;
$('.system-menu li a').click(function(){
	//切换系统
	ToggleSystem($(this).attr('systemId'),$(this).attr('systemUrl'),$(this).attr('systemLabel'));
});
(function(){
	var currentURL = window.location.href;
	var isHad = currentURL.indexOf('upms-admin/manage/index');
	if(isHad == -1) return;
	$.ajax({
		url: "http://localhost:8080/upms-admin/manage/list",
		type: 'GET',
		success: function(result){
			if(result.code == 1){
				//初始化
				systemData = result.data;
				var system = $('.system-menu li a:first');
				ToggleSystem(system.attr('systemId'),system.attr('systemUrl'),system.attr('systemLabel'));
			}else{
				$.confirm({
					title: "请求数据失败",
					content: result.message,
					buttons: {
						confirm: {
							text: '确认',
							btnClass: "waves-effect waves-button waves-light",
						}
					}
				});
			}
		},
		error: function(error){
			$.confirm({
				theme: 'dark',
				title: '请求数据错误！请重试',
				content: error,
				buttons: {
					confirm: {
						text: '确认',
						btnClass: 'waves-effect waves-button waves-light',
					}
				}
			});
		}
	});
	
})();
/**
 * 系统切换函数
 */
function ToggleSystem(systemId,systemUrl,systemLabel){
	/*
	 * 目标：完成数据节点挂载
	 */
	//更改系统名称
	$('#system-label').text(systemLabel);
	//删除原先的数据节点
	$('.aside-menu').empty();
	$('.aside-menu').append('<li><a class="waves-effect' + '" href="javascript:Tab.addTab('+ "'首页','home'" + ');"><i class="zmdi zmdi-home"></i>首页</a></li>');
	for(var i=0; i < systemData.length; i++){
		if(systemData[i].systemId == systemId){
			var permission = systemData[i];
			if(permission.pid != 0 || permission.type != 1 ) continue;
			var li = '<li class="sub-menu menu-items_' + permission.permissionId + '"><a class="waves-effect menu-id="'+ permission.permissionId+'" href="javascript:;"><i class="' + permission.icon + '"></i>' + permission.label + '</a></li>';
			//拼接html
			$('.aside-menu').append(li);
			$('.menu-items_' + permission.permissionId).append('<ul>');
			for(var j=0;j<systemData.length;j++){
				var item = systemData[j];
				if(item.pid != permission.permissionId) continue;
				var ili = '<li><a class="waves-effect menu-item_'+ item.permissionId +'" href="javascript:Tab.addTab(' + "'" + item.label + "','" + systemUrl + item.uri + "');" + '">' +  item.label + '</a></li>';
				$('.menu-items_' + permission.permissionId + ' ul').append(ili);
			}
		
		}
	}
	$('.aside-menu').append("<li><div class='upms-version'>&copy; Lee-UPMS 1.0.0</div></li>");
		
}
