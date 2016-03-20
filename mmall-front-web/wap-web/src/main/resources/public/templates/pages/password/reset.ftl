<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "/wrap/3g.common.ftl" />
<html>
  <head>
    <title>找回密码</title>
    <@meta/>
    <@less />
  </head>
  <body>
  	 <div class="wrap">
	    <header class="headerbox">
		     <div class="m-topnav">
		        <a class="curp back" href="javascript:void(0);">
				    <i class="f-fl u-menu"></i>
			    </a>
			    <span class="tt">找回密码</span>
		  	 </div>
		    <div class="u-top" id="gotop"></div>
		</header>
		<div class="m-login reset-box">
		  <div class="step1">
		      <form action="" method="post" autocomplete="off">
			        <div class="form-group form-group-uname j-flag">
			          <div class="login-it">
			            <div class="ln ln-first f-cb">
			              <div class="ipt-con">
			                <div class="ipt-wrap">
			                  <input type="text" class="ipt user user-name" name="username" autocomplete="off" placeholder="请输入用户名" />
			                </div>
			                <a class="clear"></a>
			              </div>
			            </div>
			          </div>
			        </div>
			        <div class="form-group">
			            <a href="javascript:void(0);" class="btn btn-l j-flag submit-1">下一步</a>
			        </div>
		      </form>
	      </div>
	      <div class="step2">
	          <form action="" method="post" autocomplete="off">
		            <div class="user-title">
				        <ul>
				            <li>您正在找回账号</li>
				            <li><i class="user-2"></i>的密码</li>
				        </ul>
				    </div>
				    <div class="phone-box">
				        <ul>
				            <li>账号绑定手机：<i class="phone"></i></li>
				        </ul>
				    </div>
			        <div class="form-group form-group-uname j-flag m-b">
			          <div class="login-it">
			            <div class="ln ln-first f-cb">
			              <div class="ipt-con">
			                <div class="ipt-wrap">
			                  <input type="text" class="ipt verificy-code" autocomplete="off" placeholder="请输入短信验证码">
			                </div>
			                <a class="clear"></a>
			              </div>
			            </div>
			            <input type="button" class="valid-btn btn-1 count-down" value="获取验证码" />
			          </div>
			        </div>
			        <div class="form-group">
			           <a href="javascript:void(0);" class="btn btn-1 j-flag submit-2">下一步</a>
			        </div>
		      </form>
	      </div>
	      <div class="step3">
	          <form action="" method="post" autocomplete="off">
		            <div class="user-title set-psd">
				        <ul>
				            <li>设置新密码：</li>
				        </ul>
				    </div>
			        <div class="form-group form-group-uname j-flag m-b">
			          <div class="login-it">
			            <div class="ln ln-first f-cb ln-r">
			              <div class="ipt-con">
			                <div class="ipt-wrap">
			                  <input type="password" class="ipt new-pwd" autocomplete="off" placeholder="6到16个字符，区分大小写">
			                </div>
			                <a class="clear rp"></a>
			                <i class="show-psd"></i>
			              </div>
			            </div>
			          </div>
			        </div>
			        <div class="form-group">
			          <a href="javascript:void(0);" class="btn btn-1 j-flag submit-3">下一步</a>
			        </div>
		      </form>
	      </div>
	      <div class="step4">
	          <form action="" method="post" autocomplete="off">
		            <div class="success-tip">
		                <span class="text-success">新密码设置成功！</span>
		            </div>
			        <div class="form-group">
			            <a href="/login" class="btn btn-1 j-flag">马上去登录</a>
			        </div>
		      </form>
	      </div>
       </div>
    </div>
    <@jsFrame />
    <script>
    	$(function () {

            var userNameStatus = false,
                mobileCode = null;
    	
            // 第一页，下一步
            $(".submit-1").on("click", function () {
                var userName = $(".user-name").val();
                if(userName == "") {
                    $.message.alert("提示","请输入用户名！");
                } else {
                    $.ajax({
	    				url : "/password/reset/verifyUserName",
	    				type : "GET",
	    				dataType : "json",
	    				data : {
	    					userName : userName
	    				},
	    				success : function (data) {
	    					if (data.code == 200) {
	                            mobileCode = data.result;
	                            $(".step1").hide();
		                        $(".step2").show();
		                        $(".step2 .user-2").html(userName);
		                        $(".step2 .phone").html(mobileCode);
	    					} else {
	    						$.message.alert("提示",data.message);
	    					}
	    				}
	    			});
                }
            });

            // 获取验证码
            $(".count-down").on("click", function () {
                var userName = $(".user-name").val();
                getCode(userName);
            });

            // 获取验证码
            function getCode(userName) {
                $.ajax({
                    url : "/password/reset/sendCode",
                    type : "GET",
                    dataType : "json",
                    data : {
                        userName : userName
                    },
                    success : function (data) {
                        if (data.code == 200) {
                            countDown($(".count-down"), 45);
                        } else {
                            $.message.alert("提示",data.message);
                        }
                    }
                });
            }

            // 第二页，下一步
            $(".submit-2").on("click", function () {
                var verificyCode = $(".verificy-code").val();
                if (verificyCode == "") {
                    $.message.alert("提示","验证码不能为空！");
                } else {
                    var userName = $(".user-name").val(),
                        code = $(".verificy-code").val();
                    setCode(userName, code);
                }
            });

            // 提交验证码
            function setCode(userName, code) {
                $.ajax({
                    url : "/password/reset/third",
                    type : "GET",
                    dataType : "json",
                    data : {
                        userName : userName,
                        code : code
                    },
                    success : function (data) {
                        if (data.code == 200) {
                            $(".step2").hide();
                            $(".step3").show();
                        } else {
                            $.message.alert("提示",data.message);
                        }
                    }
                });
            }
            
            $(".step3 .show-psd").on("click", function () {
                $(this).toggleClass("hide-psd");
                if ($(this).hasClass("hide-psd")) {
                    $(".step3 .new-pwd").attr("type", "text");
                } else {
                    $(".step3 .new-pwd").attr("type", "password");
                }
            });

            // 第三页，下一步
            $(".submit-3").on("click", function () {
                var newPwd = $(".new-pwd").val(),
                    userName = $(".user-name").val();
                if (newPwd == "") {
                    $.message.alert("提示","新密码不能为空！");
                } else {
                    resetPwd(userName, newPwd);
                }
            });

            // 重置密码
            function resetPwd(userName, newPwd) {
                $.ajax({
                    url : "/password/reset/final",
                    type : "POST",
                    data : {
                        userName :  userName,
                        password : newPwd
                    },
                    success : function (data) {
                        if (data.code == 200) {
                            $(".step3").hide();
                            $(".step4").show();
                        } else {
                            $.message.alert("提示",data.message);
                        }
                    } 
                });
            }
            
            $(".back").click(function(){
                if($(".step1").is(":visible")==true){
                    history.go(-1);
                }else if($(".step2").is(":visible")==true){
                    $(".step2").hide();
                    $(".step1").show();
                }else if($(".step3").is(":visible")==true){
                    $(".step3").hide();
                    $(".step2").show();
                }else if($(".step4").is(":visible")==true){
                    $(".step4").hide();
                    $(".step3").show();
                }
            });
            
			/**
			 	*
			 	* @ 倒计时
				*
			**/
			function countDown(btn, time) {
			    var thisVal = btn.val(),
			    	time = time,
			        i = 1;
		    	start = setInterval(function () {
			        var countDownTime = time - i;
			        if (i > time - 1) {
			            i = 1;
			            $(btn).removeClass("btn-disable").attr("disabled", false).val(thisVal);
			            clearInterval(start);
			        } else {
			        	$(btn).addClass("btn-disable").attr("disabled", true).val(countDownTime + "秒后重发");
			            i++;
			        }
			    }, 1000);
			}

    	});
    </script>
  </body>
</html>
</@compress>
</#escape>