<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
        <#include "/wrap/common.ftl">
        <@title type="login" />
        <@css/>
    </head>
    <body>
        <div class="wrap login-logo">
            <a href="/">
                <img src="/src/images/logo.png">
            </a>
        </div>
        <div class="login-bg">
            <div class="wrap">
                <div class="m-login" id="loginCon">
                    <div class="tit"><span>会员登录</span></div>
                    <div class="con">
                        <form action="http://denglu.baiwandian.cn/login" method="post" class="login-form">
                            <div class="form-group">
                                <input type="text" class="input-text input-max" name="username"  placeholder="请输入用户名" />
                            </div>
                            <div class="form-group">
                                <input type="password" class="input-text input-max" name="password" placeholder="请输入密码" /> 
                            </div>
                            <div class="form-group">
                                <i class="err_icon"></i>
                                <span class="errorBox"></span>
                            </div>
                            <div class="form-group">
                                <a class="commit" tabindex="3">登&nbsp;录</a>
                            </div>
                            <div class="form-group clearfix">
                                <div class="left">
                                	<input type="checkbox" id='remember' checked="checked" />
                                	<label for="remember">记住密码</label>
                                </div>
                                <div class="right"><a href="/password/reset/first/" class="h-link">忘记密码?</a></div>
                            </div>
                            <input type="hidden" name="redirectError" class="redirectError" value="http://023.baiwandian.cn/login"/>
                            <input type="hidden" name="redirectURL" class="redirectURL" value=""/>
                            <input type="hidden" name="type" class="type" value="2"/>
                        </form> 
                    </div>
                    <div class="login-tips">公共场所不建议使用记住密码，以防账号丢失</div>
                </div>
            </div>
        </div>
        <@footer/>
        <@copyright />
        <@js />
        <script type="text/javascript">
            $(function () {
                 
                var username 	= $("input[name='username']"),
                    password 	= $("input[name='password']"),
                    submit   	= $(".commit"),
                    errorBox 	= $(".errorBox"),
                    redirectURL = "http://023.baiwandian.cn",
                    url         = window.location.href;

                if (url.indexOf("redirectURL") > 0) {
                    redirectURL = decodeURIComponent(url.substring(url.indexOf("redirectURL") + 12, url.length));
                }
                $("input[name='redirectURL']").val(redirectURL);
				
            	username.val(getCookie("mainsite-username"));
            	password.val(getCookie("mainsite-password"));
            	
				if (getCookie("mainsite-remember") == "") {
					document.getElementById("remember").checked = true;
            	} else {
            		document.getElementById("remember").checked = getCookie("mainsite-remember");
            	}
            	
                username.on("blur", function () {
                   if ($.trim(username.val()) == "") {
                        username.addClass("input-error");
                        errorBox.html("您还没有输入用户名哦").show();
                    } else {
                        username.removeClass("input-error");
                    }
                });

                password.on("blur", function () {
                    if ($.trim(username.val()) == "") {
                        username.addClass("input-error");
                        errorBox.html("您还没有输入用户名哦").show();
                        username.focus();
                    } else if ($.trim(password.val()) == "") {
                        username.removeClass("input-error");
                        password.addClass("input-error");
                        errorBox.html("您还没有输入密码哦").show();
                    } else {
                        password.removeClass("input-error");
                        errorBox.hide();
                    }
                });

                submit.on("click", function () {
                    checkUser();
                });

                // 回车键登陆
                $(document).on("keyup", function (e) {
                    if (e.which == 13) {
                        checkUser();
                    }
                }); 

                // 获取cookie错误信息
                function getCookieInfo() {
                    var errInfo = getCookie("XYLERR");
                    if (errInfo != null && errInfo != "") {
                        errorBox.html(decodeURIComponent(errInfo)).show();
                    }
                }
                getCookieInfo();

                // 验证用户名或密码不能为空
                function checkUser() {
                    var remember = document.getElementById("remember");
    				var checked = remember.checked;
                    if ($.trim(username.val()) == "") {
                        username.addClass("input-error");
                        errorBox.html("您还没有输入用户名哦").show();
                    } else if ($.trim(password.val()) == "") {
                        username.removeClass("input-error");
                        password.addClass("input-error");
                        errorBox.html("您还没有输入密码哦").show();
                    } else {
                        password.removeClass("input-error");
                        errorBox.hide();
                        if(checked){
		        			setCookie("mainsite-username",username.val());
		        			setCookie("mainsite-password",password.val());
		        			setCookie("mainsite-remember",checked);
		        		}else{
		        			delCookie("mainsite-username",username.val());
		        			delCookie("mainsite-password",password.val());
		        			delCookie("mainsite-remember",checked);
		        		}
                        $(".login-form").submit();
                    }
                }              

            });
	        var getCookie = function(cname){
	        	var name = cname + "=";
			    var ca = document.cookie.split(';');
			    for(var i=0; i<ca.length; i++) {
			        var c = ca[i];
			        while (c.charAt(0)==' ') c = c.substring(1);
			        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
			    }
			    return "";
	        }; 
	        var setCookie = function(name,value) { 
	            var Days = 30; 
	            var exp = new Date(); 
	            exp.setTime(exp.getTime() + Days*24*60*60*1000); 
	            document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString(); 
	        }; 

	        var delCookie = function(name,value){ 
	            var date = new Date(); 
	            date.setTime(date.getTime() - 10000); 
	            document.cookie = name + "="+ escape (value) +"; expires=" + date.toGMTString(); 
	        };
        </script>
    </body>
</html>
</@compress>
</#escape>
