<#include "mobile.common.ftl">

<!DOCTYPE html>
<html>
	<head>
		<#--meta标签-->
		<@headMeta/>
		<#--@CSS-->
		<link type="text/css" href="/mobile/css/mobileLogin.css" rel="stylesheet" />
		<title>登陆新品购</title>
		<!--[if lt IE 8]>      <![endif]-->
	</head>
	<body>
		<noscript></noscript>
		<#--container-->
		<div class="g-container">
			<#--头部banner-->
			<div class="g-banner">
				<div class="m-banner-sawtooth"></div>
			</div>
			<#--登录 form-->
			<div class="g-login">
				<form id="loginForm" action="https://reg.163.com/logins.jsp" type="post">
					<#--错误提示区域-->
					<div class="m-login-err"></div>
					<div class="m-input-container">
						<#--用户名-->
						<div class="m-login-input"> 
							<div class="m-input-tip">
								<span class="m-tip-log username"></span>
							</div>
							<div class="m-input-wrap">
								<div class="m-wrap-data">
									<input type="text" name="username" placeholder="请输入网易通行证" class="m-login-item" id="m-login-username" />
								</div>
							</div>
						</div>
						<#--密码-->
						<div class="m-login-input">
							<div class="m-input-tip">
								<span class="m-tip-log password"></span>
							</div>
							<div class="m-input-wrap">
								<div class="m-wrap-data">
									<input type="password" name="password" placeholder="请输入密码" class="m-login-item" id="m-login-password" />
								</div>
							</div>
						</div>
					</div>
					<#--登录信息验证-->
					<input type="hidden" name="type" value="1" />
					<input type="hidden" name="url" id="jumpUrl" />
					<#--非登录操作-->
					<div class="g-login-register">
						<div class="m-register-table">
							<span class="m-register-cell f-tal"><a target="_blank" href="http://reg.163.com/resetpwd/index.do">忘记密码？</a></span>
							<span class="m-register-cell f-tar"><a href="http://reg.163.com/reg/reg.jsp">注册</a></span>
						</div>
					</div>
					<#--登录-->
					<div class="m-submit-wrap">
						<a href="javascript:;" class="login" id="m-login-submit">登 录</a>
					</div>
				</form>
			</div>
			<#--第三方登录-->
			<div class="g-login-other">
				<div class="m-lother-title">
					<span>第三方登录</span>
				</div>
				<div class="loginLogo">
					<#--第三方登录1-->
					<a target="_blank" href="javascript:void(0);" data-type="8"> <span class="m-lother yixin"></span>
						<p>易信</p>
					</a>
					<#--第三方登录2-->
					<a target="_blank" href="javascript:void(0);" data-type="1"> <span class="m-lother qq"></span>
						<p>QQ</p>
					</a>
					<#--第三方登录3-->
					<a target="_blank" href="javascript:void(0);" data-type="3"> <span class="m-lother weibo"></span>
						<p>新浪微博</p> 
					</a>
				</div>
			</div>
		</div>
		<#--@script-->
		<script src="/mobile/javascript/lib/nej/src/define.js?pro=/mobile/javascript"></script>
		<script src="/mobile/javascript/page/login.js"></script>
	</body>
</html>