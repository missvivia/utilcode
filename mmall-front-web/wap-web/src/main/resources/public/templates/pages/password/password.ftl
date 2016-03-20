<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "/wrap/3g.common.ftl" />
<html>
  <head>
    <title>修改密码</title>
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
			    <span class="tt">修改密码</span>
		  	 </div>
		    <div class="u-top" id="gotop"></div>
		</header>
		<div class="m-login modify-pwd reset-box">
		      <form action="" method="post" autocomplete="off">
			        <div class="form-group form-group-uname j-flag">
			          <div class="login-it">
			            <div class="ln ln-first f-cb">
			              <div class="ipt-con">
			                <div class="ipt-wrap">
			                  <input type="password" class="ipt user user-name old-pwd" id="oldPsd" autocomplete="off" placeholder="原密码" />
			                </div>
			              </div>
			              <i class="btn-1 show-btn show-icon-0"></i>
			            </div>
			            <div class="ln ln-first f-cb">
			              <div class="ipt-con">
			                <div class="ipt-wrap">
			                  <input type="password" class="ipt user user-name new-pwd" id="psd" autocomplete="off" placeholder="新密码" />
			                </div>
			              </div>
			              <i class="btn-1 show-btn show-icon-1"></i>
			            </div>
			            <div class="ln f-cb">
			              <div class="ipt-con">
			                <div class="ipt-wrap">
			                  <input type="password" class="ipt psd confirm-pwd" id="psdAgain" autocomplete="off" placeholder="新密码确认" >
			                </div>
			              </div>
			              <i class="btn-1 show-btn show-icon-2"></i>
			            </div>
			          </div>
			        </div>
			        <div class="form-group commit-group">
			            <a href="javascript:void(0);" class="btn btn-l j-flag commit">确定</a>
			        </div>
		      </form>
	      </div>
       </div>
    </div>
    <@jsFrame />
    <script>
    	$(function () {
    	    $(".show-icon-0").click(function(){
    	         $(this).toggleClass("hide-icon");
    	         if ($(this).hasClass("hide-icon")) {
    	             $("#oldPsd").attr("type","text");
    	         }else{
    	             $("#oldPsd").attr("type","password");
    	         }
    	    });
    	    
    	    $(".show-icon-1").click(function(){
    	         $(this).toggleClass("hide-icon");
    	         if ($(this).hasClass("hide-icon")) {
    	             $("#psd").attr("type","text");
    	         }else{
    	             $("#psd").attr("type","password");
    	         }
    	    });
    	    
    	    $(".show-icon-2").click(function(){
    	         $(this).toggleClass("hide-icon");
    	         if ($(this).hasClass("hide-icon")) {
    	             $("#psdAgain").attr("type","text");
    	         }else{
    	             $("#psdAgain").attr("type","password");
    	         }
    	    });
    	    
    	    $(".old-pwd").on("blur", function () {
				var _this = $(this),
					pwd = _this.val();
				if (pwd != "") {
					verificationOldPwd(pwd);
				}
			});
			
			function verificationOldPwd(pwd) {
				$.ajax({
					url  : "/password/verify",
					type : "GET",
					async: false,
					dataType: "json",
					data: {
                    	oldPass : pwd
                    },
					success : function (data) {
						if (data.code != 200) {
							$.message.alert("提示","原密码错误！");
						}
					}
				});
		    }
    	    
    	    $(".commit").click(function(){
    	         var oldpwd = $(".old-pwd").val(),
    	             newpwd = $(".new-pwd").val(),
    	             confirmpwd = $(".confirm-pwd").val(),
    	             newPass = /^[0-9a-zA-Z_]{6,20}$/;
    	         if(oldpwd == ""){
    	             $.message.alert("提示","请输入原密码！",function(){
    	                 $(".old-pwd").focus();
    	             });
    	             return;
    	        }else if(newpwd == ""){
    	             $.message.alert("提示","请输入新密码！",function(){
    	                 $(".new-pwd").focus();
    	             });
    	             return;
    	         }else if(!newPass.test(newpwd)) {
					 $.message.alert("提示","请输入6-20位的密码！");
					 return;
				 }else if(confirmpwd == ""){
    	             $.message.alert("提示","请再次输入新密码！",function(){
    	                 $(".confirm-pwd").focus();
    	             });
    	             return;
    	         }else if(newpwd != confirmpwd) {
					 $.message.alert("提示","请输入相同密码！");
					 return;
				 }
    	     
    	         modifyPwd(oldpwd, newpwd, confirmpwd);
    	    });
			
			function modifyPwd(oldPass, newPass, confirmPass) {
				$.ajax({
					url : "/password/modify",
					type : "POST",
					dataType : "json",
					data : {
						oldPass : oldPass,
						newPass : newPass,
						confirmPass : confirmPass
					},
					success : function (data) {
						if (data.success == 1) {
							$.message.alert("提示","修改成功，请牢记新的登录密码！",function(){
							     window.location.href = "/profile/basicinfo";
							});
						} else {
							$.message.alert("提示","修改失败，请重新编辑！");
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