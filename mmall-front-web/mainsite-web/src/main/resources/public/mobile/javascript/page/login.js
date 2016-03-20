/**
 *登录验证 + 第三方登录 
 * 
 * 
 * @author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
	'{pro}/lib/wap/wap.js' // 对应对象_wap(基础工具函数)
], function(_wap, p, o, f, r) {
	_wap._$domReady(function() {
	    /*==============================================
		* 通行证
		==============================================*/
		var 
			subForm = _wap._$('#m-login-submit'), // 登录按钮
			errMsg = _wap._$('.m-login-err'), // 错误提示
			_redirectURL = _wap._$getUrlParam('redirectURL'); // 跳转链接

		// 设置登录跳转
		_wap._$('#jumpUrl').value = _redirectURL;

		/* 基础验证：用户名不为空 */
		function checkUsername() {
			var username = _wap._$('#m-login-username').value.trim();
			if (username == "") {
				errMsg.innerHTML = '用户名不能为空';
				errMsg.style.display = 'block';
				return false;
			}
			return true;
		}

		/* 基础验证：密码不为空 */
		function checkPassword() {
			var password = _wap._$('#m-login-password').value.trim();
			if (password == "") {
				errMsg.innerHTML = '密码不能为空';
				errMsg.style.display = 'block';
				return false;
			}
			return true;
		}
		
		/*focus事件绑定触发 隐藏错误提示*/
		_wap._$addEventListener(_wap._$('#m-login-username'), 'focus', hideErrmsg);
		_wap._$addEventListener(_wap._$('#m-login-password'), 'focus', hideErrmsg);

		/*隐藏错误提示*/
		function hideErrmsg() {
			errMsg.style.display = 'none';
		}

		/*登录触发*/
		subForm.onclick = function() {
			if (!checkUsername() || !checkPassword()) {
				return false;
			}
			_wap._$('#loginForm').submit();
		}
		
		/*==============================================
		* 第三方登录
		==============================================*/
		var loginBtns = _wap._$('.loginLogo a'); // 第三方登录按钮
		
			/*点击登录*/
			loginBtns._$forEach(function(btn) {
			// 点击绑定
			btn.onclick = function() {
				var _type = this.getAttribute('data-type'); // 第三方登录类型
				// 第三方登录跳转
	        	location.href = '/ext/login/' + _type + '?redirectURL=' + encodeURIComponent(_redirectURL);
			}
		})
		
	})
})