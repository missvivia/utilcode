<#escape x as x?html>
<@compress single_line=true>
<!DOCTYPE html>
<html>
<head>
<#include "/wrap/common.ftl">
<meta charset="utf-8"/>
<title>登录</title>
<#include "/wrap/css.ftl">
<style>
  body{background-color:#fff;}
  body{background-color:#fff;font:helvetica,"Hiragino Sans GB","Microsoft YaHei","微软雅黑","宋体",Arial;color:#333;}
  .modal-body{padding-bottom:30px;}
  .g-hd{background:#f5f5f5;}
  .m-topnav{height:102px;width:1090px;margin:0 auto;}
  .m-topnav .logo{float:left;padding:12px 0 0;}
  .m-topnav .logo h1{display:block;width:158px;height:41px;overflow:hidden;text-indent:-2000px;background:url(/res/images/logo_new.png) 0 0 no-repeat;}
  li {list-style:none;}
  .login-con{margin: 150px auto;min-width:320px;width:33%;height:380px;}
  .login-con .left {float:left;}
  .login-con .right{float:right;}
  .login-btn{display:block;width:100%;border-radius:6px;border:1px solid #0e74d5;background: -moz-linear-gradient( top,#0e74d5,#0e74d5);background: -webkit-linear-gradient( top,#0e74d5,#0e74d5);}
  .login-tt{font-size:28px;color:#fff;padding-bottom:10px;}
  .login-tab{margin-bottom:20px;}
  .input-group-lg > .form-control, .input-group-lg > .input-group-addon, .input-group-lg > .input-group-btn > .btn {font-size: 16px;height:46px;}
  .login-con ul.txtlist{position:absolute;top:45px;left:64px;padding:1px 0;z-index:100;width:75%;background-color:#fff;border:1px solid #d7d7d7;}
  .login-con ul.txtlist li{padding: 0 12px;height:32px;line-height:32px;cursor:pointer;}
  .login-con ul.txtlist li.unit{font-size: 14px;color:#8f8e94;}
  .login-con ul.txtlist li.tip{font-size: 12px;color:#696864;}
  .login-con ul.txtlist li.js-selected{background-color:#e7e7e7;}
  .login-con .ipt:-moz-placeholder {color: #cccccc;}
  .login-con .ipt::-moz-placeholder {color: #cccccc;opacity: 1;}
  .login-con .ipt:-ms-input-placeholder {color: #cccccc;}
  .login-con .ipt::-webkit-input-placeholder {color: #cccccc;}
  .login-con .js-placeholder {font-size:12px;color:#ccc;position:absolute;top:0;*top:10px;padding-left:33px;}
  .m-copyright{padding:50px 0;line-height:22px;text-align:center;color:#999;}
  .m-copyright a,.m-copyright a:hover{color:#999;}
  .logo{background : url(/res/images/logo-login-xsm.png) no-repeat 0 0; padding-left:170px;margin:0 auto 34px auto;width:290px;}
  .logo h1{font-family:"Microsoft YaHei";font-size:30px;font-weight:bold;padding-top:13px;}
  .logo h6{margin:0;font-family:"Microsoft YaHei";font-weight:bold;padding-bottom:15px;}
  .login-con .checkbox{padding-top:0;}
  .form-control{height:46px;}
  .input-group,.input-group .form-control,.js-mhd-parent{position:static;}
  .js-mhd-parent{position:static !important;}
  .col-sm-10{position:static;}
  span.js-error {display:block;width:100%; color: #d9534f;position:absolute;left:0;top:370px;background:#fff;text-align:center;}
  .user-input-group span.js-error{z-index:99;}
  .pwd-input-group span.js-error{z-index:98;}
  #errorBox{display:block;z-index:97;}
</style>
</head>
<body>
<div class="container-fluid">
  <div class="row">
    <div class="login-con">
      <div class="modal-content">
        <div class="modal-body">
          <div class="logo">
            	<h1>商家平台</h1>
            	<h6>Merchant platform</h6>
          </div>
          <form class="form-horizontal"  autocomplete="off" id="loginform-p" method="post">
            <div class="form-group">
              <div class="col-sm-10 col-sm-offset-1">
                <div class="input-group input-group-lg user-input-group">
                  <span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
                  
                  <input type="text" class="form-control ft16 ipt user" placeholder="请输入帐号" id="namePrefix" data-holder="js-placeholder" data-required="true" data-message="帐号不能为空"/>
                </div>
                <!--
                <ul class="txtlist sug" style="visibility:hidden;">
                    <li class="tip">请选择..</li>
                    <li class="unit js-selected">qajjc@163.com</li>
                    <li class="unit">qajjc@126.com</li>
                </ul>
                -->
              </div>
            </div>
            <div class="form-group">
              <div class="col-sm-10 col-sm-offset-1">
                <div class="input-group input-group-lg pwd-input-group">
                  <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                  <input type="password" class="form-control ft16 ipt psd" placeholder="请输入密码" id="password" data-holder="js-placeholder" data-required="true" data-message="密码不能为空">
                </div>
              </div>
            </div>
            <div class="form-group">
				<div class="col-sm-10 col-sm-offset-1">
					<div class="checkbox left">
					    <label>
					      <input type="checkbox" id='remember' checked>记住帐号
					    </label>
					</div>
					<div class="right">
						如忘记密码请<a href="javascript:;" id="contactAdmin">联系管理员</a>
	<!--	              
		              <a href="#" target="_blank">忘记密码</a>
		              <span>|</span>
		              <a href="#" target="_blank">注册</a>
		            </div>
	-->              
	            	</div>
	            </div>
            </div>
            <div class="form-group">
              <div class="col-sm-10 col-sm-offset-1">
                <input type="hidden" id="redirectError" name="redirectError" />
                <input type="hidden" id="redirectURL" name="redirectURL" />
                <input type="hidden" id="type" name="type" value="1" />
                <input type="hidden"  name="username" id="username"/>
                <input type="hidden"  name="password" id="pwd"/>
                <button class="btn btn-primary btn-lg login-btn commit">立即登录</button>
                
                <span class="js-error" id="errorBox"></span>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/login.js?v=1.0.0.0"></script>
</body>
</html>
</@compress>
</#escape>