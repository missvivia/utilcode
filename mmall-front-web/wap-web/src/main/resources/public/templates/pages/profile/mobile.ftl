<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "/wrap/3g.common.ftl" />
<html>
  <head>
    <title>修改手机</title>
    <@meta/>
    <@less />
  </head>
  <body>
  	 <div class="wrap">
	    <header class="headerbox">
		     <div class="m-topnav">
		        <a class="curp go-back">
				    <i class="f-fl u-menu"></i>
			    </a>
			    <span class="tt">修改手机</span>
		  	 </div>
		    <div class="u-top" id="gotop"></div>
		</header>
		<div class="m-login reset-box mobile-box">
		      <form action="" method="post" autocomplete="off">
			        <div class="form-group form-group-uname j-flag">
			          <div class="login-it">
				            <div class="ln f-cb first">
				              <div class="ipt-con">
				                <label class="label">手机号</label>
				                <div class="ipt-wrap">
				                  <input type="text" class="ipt mobile-code" autocomplete="off" placeholder="请填写手机号" />
				                </div>
				                <a class="clear"></a>
				              </div>
				            </div>
				            
				            <div class="ln f-cb second">
				              <div class="ipt-con">
				                    <label class="label">验证码</label>
					                <div class="ipt-wrap">
					                    <input type="text" class="ipt verification-code" autocomplete="off" placeholder="请填写验证码" >
					                </div>
					                <input type="button" class="valid-btn btn-1 count-down" value="获取验证码" />
					                <a class="clear"></a>
				              </div>
				            </div>
			          </div>
			        </div>
			        <div class="form-group commit-group">
			            <a href="javascript:void(0);" class="btn btn-l j-flag commit">确认修改</a>
			        </div>
		      </form>
	      </div>
       </div>
    </div>
    <@jsFrame />
    <script>
    	$(function () {
    	    var mobileReg = /^1[3|5|7|8|][0-9]{9}$/;
    	    $(".count-down").on("click", function () {
                var code = $(".mobile-code").val();
                if (code == "") {
                    $.message.alert("提示","请输入手机号码！");
                } else if (mobileReg.test(code) == false) {
                    $.message.alert("提示","请输入正确的手机号码！");
                } else {
                    getCode(code);
                }
            });

            // 获取验证码
            function getCode(code) {
                $.ajax({
                    url : "/mobile/getCode",
                    type : "GET",
                    dataType : "json",
                    data : {
                        mobile : code
                    },
                    success : function (data) {
                        if (data.code == 200) {
                            countDown($(".count-down"), 45);
                            $.message.alert("提示","验证码已发送，15分钟内输入有效！");
                        } else {
                            $.message.alert("提示",data.message);
                        }
                    }
                });  
            }
            
            /**
			 	*
			 	* @ 倒计时
				*
			**/
			function countDown(btn, time) {
			    var thisVal = btn.val(),
			    	time = time,
			    	i = 1,
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
			
			 // 绑定新号码
            $(".commit").on("click", function () {
                var mobile = $(".mobile-code").val(),
                    code = $(".verification-code").val();
                if (mobile == "" || code == "" || mobileReg.test(mobile) == false) {
                    $.message.alert("提示","请填写正确的手机号码和验证码！");
                } else {
                    bindMobile(mobile, code);
                }
            });

            function bindMobile(mobile, code) {
                $.ajax({
                    url : "/mobile/bind",
                    type : "GET",
                    dataType : "json",
                    data : {
                        mobile : mobile,
                        code : code
                    },
                    success : function (data) {
                        if (data.code == 200) {
                            $.message.alert("提示","手机号码修改成功",function(){
                                 window.location.href = "/profile/basicinfo";
                            });
                        } else {
                            $.message.alert("提示",data.message);
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