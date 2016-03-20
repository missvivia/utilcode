<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "/wrap/common.ftl">
    <#include "/wrap/my.common.ftl">
    <@title type="resetPwd"/>
    <@css/>
  </head>
  <body>
  	<div class="wrap reset-top">
  		<a href="/"><img src="/src/images/logo.png" /></a>
  		<span>遇到问题？请拨打400-800-2222联系客服</span>
  	</div>
  	<div class="reset-line">
  		<div class="wrap">
  			<div class="step step-1">
  				<h2>请输入您要重置密码的用户名</h2>
  				<p>
  					<span>用户名：</span>
  					<input type='text' placeholder='请输入用户名' class="input-text input-max user-name" />
  					<em class="res-tips"></em>
  				</p>
  				<a href="javascript:void(0);" class="submit submit-1">下一步</a>
  			</div>
            <!-- E 第一页，验证用户名是否存在，以及是否绑定手机号 -->
            <div class="step step-2">
                <h2>我们将发送一条短信验证码到您的手机</h2>
                <p>
                    <span>手机号：</span>
                    <input type='text' class="input-text input-max mobile-code input-disable" disabled="disabled"  />
                </p>
                <a href="javascript:void(0);" class="submit submit-2">发送验证码</a>
            </div>
            <!-- E 第二页，显示绑定的手机号 -->
            <div class="step step-3">
                <h2>短信验证码已经发送到您的手机（验证码有效期15分钟）</h2>
                <p>
                    <span>手机号：</span>
                    <input type='text' class="input-text input-max mobile-code input-disable" disabled="disabled"  />
                </p>
                <p>
                    <span>验证码：</span>
                    <input type='text' class="input-text input-min verificy-code" placeholder="6位验证码" />
                    <input type="button" class="btn-base btn-cancel count-down" value="获取验证码" />
                    <em class="res-tips"></em>
                </p>
                <a href="javascript:void(0);" class="submit submit-3">下一步</a>
            </div>
            <!-- E 第三页，绑定验证码 -->
            <div class="step step-4">
                <h2>输入一个新的密码</h2>
                <p class="clearfix">
                    <span>新密码：</span>
                    <i class="show-pwd">
                        <input type='password' class="input-text input-max new-pwd" placeholder="请输入6-20位数字或字母" />
                        <em class="res-tips"></em>
                        <b style="diplay: none;">显示</b>
                    </i>
                </p>
                <a href="javascript:void(0);" class="submit submit-4">确定</a>
            </div>
            <!-- E 第四页，输入新密码，显示密码 -->
            <div class="step step-5">
                <h3>密码修改成功！请妥善保管</h3>
                <p>
                    <a href="/login" class='go-login'>前往登录</a>
                    <a href="/" class="go-home">返回首页</a>
                </p>
            </div>
            <!-- E 第五页，修改成功 -->
  		</div>
  	</div>
    <@footer/>
    <@copyright />
    <@cityChange />
    <div class="popup"></div>
    <@js />
    <script>
    	$(function () {

            var userNameStatus = false,
                mobileCode = null;
    	
    		// 验证用户名
    		$(".step-1 .user-name").on("change", function () {
    			var userName = $(this).val(),
    				_this = $(this);
    			if (userName == "") {
    				$(this).addClass("input-error").siblings(".res-tips").addClass("tips-err").removeClass("tips-suc").show().html("请输入用户名！");
    			} else {
    				verifyUserName(userName, _this);
    			}
    		});
    		
    		function verifyUserName(userName, _this) {
    			$.ajax({
    				url : "/password/reset/verifyUserName",
    				type : "GET",
    				dataType : "json",
    				data : {
    					userName : userName
    				},
    				success : function (data) {
    					//console.log(data);
    					if (data.code == 200) {
    						_this.removeClass("input-error").siblings(".res-tips").hide();
                            userNameStatus = true;
                            mobileCode = data.result;
    					} else {
                            userNameStatus = false;
    						_this.addClass("input-error").siblings(".res-tips").addClass("tips-err").removeClass("tips-suc").show().html(data.message);
    					}
    				}
    			});
    		}

            // 第一页，下一步
            $(".submit-1").on("click", function () {
                //console.log(userNameStatus);
                var userName = $(".user-name").val();
                if(userName == "") {
                    $(".user-name").addClass("input-error");
                    $(".step-1 .res-tips").addClass("tips-err").removeClass("tips-suc").show().html("请输入用户名！");
                } else {
                    if (userNameStatus) {
                        //getMobile(userName);
                        $(".step-1").hide();
                        $(".step-2").show();
                        $(".step-2 .mobile-code").val(mobileCode);
                    }
                }
            });

            // 第二页，下一步
            $(".submit-2").on("click", function () {
                var userName = $(".user-name").val();
                getCode(userName);
            });

            // 获取验证码
            function getCode(userName) {
                //console.log(mobileCode);
                $.ajax({
                    url : "/password/reset/sendCode",
                    type : "GET",
                    dataType : "json",
                    data : {
                        userName : userName
                    },
                    success : function (data) {
                        //console.log(data);
                        if (data.code == 200) {
                            $(".step-3 .mobile-code").val(mobileCode);
                            $(".step-2").hide();
                            $(".step-3").show();
                            countDown($(".count-down"), 45);
                        } else {
                            $.message.alert(data.message, "info");
                        }
                    }
                });
            }

            // 重新获取验证码
            $(".count-down").on("click", function () {
                var userName = $(".user-name").val();
                getCode(userName);
                countDown($(this), 45);
            });

            // 第三页，下一步
            $(".submit-3").on("click", function () {
                var verificyCode = $(".verificy-code").val();
                if (verificyCode == "") {
                    $(this).closest(".step").find(".res-tips").show().addClass("tips-err").html("验证码不能为空！");
                } else {
                    $(this).closest(".step").find(".res-tips").hide();
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
                        //console.log(data);
                        if (data.code == 200) {
                        //if (data.code == 400) {
                            $(".step-3").hide();
                            $(".step-4").show();
                        } else {
                            $.message.alert(data.message, "info");
                        }
                    }
                });
            }

            // 密码显示隐藏
            // $(".show-pwd .new-pwd").on("keyup", function () {
            // 	var defVal = this.defaultValue,
            // 		nowVal = $(this).val();
            // 	if (nowVal != defVal) {
            // 		$(".show-pwd b").show();
            // 	} else {
            // 		$(".show-pwd b").hide();
            // 	}
            // });
            
            $(".show-pwd b").on("click", function () {
                $(this).toggleClass("show");
                var thisVal = $(".show-pwd input").val();
                if ($(this).hasClass("show")) {
                    $(this).html("隐藏");
                    $(this).siblings().attr("type", "text");
                } else {
                    $(this).html("显示");
                    $(this).siblings().attr("type", "password");
                }
            });

            // var newVal = "",
            //     btn = $(".show-pwd b");

            // $(".new-pwd").on("keyup", function () {
            //     newVal = $(this).val();
                
            //     console.log(newVal);
            //     if(btn.hasClass("show")) {
            //         $(this).remove();
            //         var dom = "<input type='text' class='input-text input-max new-pwd' value='"+ newVal +"' />";
            //         $(".show-pwd").append(dom);
            //     } else {
            //         var dom = "<input type='password' class='input-text input-max new-pwd' value='"+ newVal +"' />";
            //         $(".show-pwd").append(dom);
            //     }
            // });

            // btn.on("click", function () {
            //     $(this).toggleClass("show");
            //     if ($(this).hasClass("show")) {
            //         $(this).html("隐藏");
            //         //var dom = "<input type='text' class='input-text input-max new-pwd' value='"+ newVal +"' />";
            //         //$(".show-pwd").append(dom);
            //     } else {
            //         $(this).html("显示");
            //         //var dom = "<input type='password' class='input-text input-max new-pwd' value='"+ newVal +"' />";
            //         //$(".show-pwd").append(dom);
            //     }
            // });

            // 第四页，确定按钮
            $(".submit-4").on("click", function () {
                var newPwd = $(".new-pwd").val(),
                    userName = $(".user-name").val();
                if (newPwd == "") {
                    $(this).closest(".step").find(".res-tips").show().addClass("tips-err").html("新密码不能为空！");
                } else {
                    $(this).closest(".step").find(".res-tips").hide();
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
                        //console.log(data);
                        if (data.code == 200) {
                        //if (data.code == 400) {
                            $(".step-4").hide();
                            $(".step-5").show();
                        } else {
                            $.message.alert(data.message, "info");
                        }
                    } 
                });
            }

    	});
    </script>
  </body>
</html>
</@compress>
</#escape>