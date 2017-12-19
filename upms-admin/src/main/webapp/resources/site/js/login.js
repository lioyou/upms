$(function() {
	// Waves初始化
	Waves.displayEffect();
	// 输入框获取焦点后出现下划线
	$('.form-control').focus(function() {
		$(this).parent().addClass('fg-toggled');
	}).blur(function() {
		$(this).parent().removeClass('fg-toggled');
	});
});
//Checkbix.init();
$(function() {
	// 点击登录按钮
	$('#login-bt').click(function() {
//		login();
		validate();
	});
	// 回车事件
	$('#username, #password').keypress(function (event) {
		if (13 == event.keyCode) {
//			login();
			validate();
		}
	});
});
//验证
function validate(){
	$("#login").validate({
		 rules: {
		      username: {
		        required: true,
		        minlength: 2
		      },
		      password: {
		        required: true,
		        minlength: 5
		      },
		 },
		 messages: {
		      username: {
		        required: "请输入用户名",
		        minlength: "用户名必需由两个字母组成"
		      },
		      password: {
		        required: "请输入密码",
		        minlength: "密码长度不能小于 5 个字母"
		      },
		 },

         submitHandler: function () {
			 login();
		 }
	});
}
// 登录
function login() {
	$.ajax({
		url: basePath + '/sso/login',
		type: 'POST',
		data: {
			username: $('#username').val(),
			password: $('#password').val(),
			backurl: window.location.href
		},
		beforeSend: function() {

		},
		success: function(result){
			if (result.code == 1) {
				location.href = basePath + result.data;
			} else {
				$.confirm({
					theme: 'dark',
					animation: 'rotateX',
					closeAnimation: 'rotateX',
					title: '登录错误',
					content: result.data,
					buttons: {
						confirm: {
							text: '确认',
							btnClass: 'waves-effect waves-button waves-light'
						}
					}
				});
				if (1001 == result.code) {
					$('#username').focus();
				}
				if (1002 == result.code) {
					$('#password').focus();
				}
			}
		},
		error: function(error){
			console.log(error);
		}
	});
}