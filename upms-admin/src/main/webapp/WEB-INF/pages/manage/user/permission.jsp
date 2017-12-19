<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="permissionDialog" class="crudDialog">
	<form id="permissionForm" method="post">
		<div class="row">
			<div class="col-sm-6">
				<label>加权限</label>
				<div class="form-group">
					<div class="fg-line">
						<ul id="ztree1" class="ztree"></ul>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<label>减权限</label>
				<div class="form-group">
					<div class="fg-line">
						<ul id="ztree2" class="ztree"></ul>
					</div>
				</div>
			</div>
		</div>
		<div class="form-group text-right dialog-buttons">
		    <a class="btn btn-info" href="javascript:;" onclick="actionSubmit('/manage/user/permission',{datas: JSON.stringify(changeDatas1.concat(changeDatas2)), permissionUserId: permissionUserId});">保存</a>
			<a class="btn btn-info" href="javascript:;" onclick="dialog.close();">取消</a>
		</div>
	</form>
</div>
<script>
	var changeDatas1 = [];
	var setting1 = {
		check: {
			enable: true,
			// 勾选关联父，取消关联子
			chkboxType: { "Y" : "ps", "N" : "s" }
		},
		async: {
			type: 'get',
			enable: true,
			url: '${basePath}/manage/user/permission/' + permissionUserId + '?type=1'
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onCheck: function() {
				var zTree = $.fn.zTree.getZTreeObj("ztree1")
				var changeNodes = zTree.getChangeCheckedNodes();
				changeDatas1 = [];
				for (var i = 0; i < changeNodes.length; i ++) {
					var changeData = {};
					changeData.id = changeNodes[i].id;
					changeData.checked = changeNodes[i].checked;
					changeData.type = 1;
					changeDatas1.push(changeData);
				}
			}
		}
	};
	var changeDatas2 = [];
	var setting2 = {
		check: {
			enable: true,
			chkboxType: { "Y" : "ps", "N" : "s" }
		},
		async: {
			type: 'get',
			enable: true,
			url: '${basePath}/manage/user/permission/' + permissionUserId + '?type=-1'
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onCheck: function() {
				var zTree = $.fn.zTree.getZTreeObj("ztree2")
				var changeNodes = zTree.getChangeCheckedNodes();
				changeDatas2 = [];
				for (var i = 0; i < changeNodes.length; i ++) {
					var changeData = {};
					changeData.id = changeNodes[i].id;
					changeData.checked = changeNodes[i].checked;
					changeData.type = -1;
					changeDatas2.push(changeData);
				}
				console.log("data is " + changeData);
			}
		}
	};
	function initTree() {
		$.fn.zTree.init($('#ztree1'), setting1);
		$.fn.zTree.init($('#ztree2'), setting2);
	}

</script>