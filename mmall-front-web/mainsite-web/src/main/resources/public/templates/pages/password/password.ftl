<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "/wrap/common.ftl">
    <#include "/wrap/my.common.ftl">
    <@title type="modifyPwd"/>
    <@css/>
  </head>
  <body>
    <@fixedTop />
    <@topbar />
    <@top />
    <@mainCategory />
    <div class="bg-french-gray clearfix">
		<@crumbs>
			<a href="/index">首页</a><span>&gt;</span><a href="/profile/basicinfo">个人中心</a><span>&gt;</span><span class="selected">修改密码</span>
		</@crumbs>
     	<@myModule sideIndex=3>
			<div class="m-box m-main l my-pwd">
				<div class="hd">
					<h3>修改密码</h3>
				</div>
				<div class="content">
					<label>
						<span>当前登录密码：</span>
						<input type="password" class="input-text input-max old-pwd" />
						<em class="tips-box"></em>
					</label>
					<label>
						<span>新的登录密码：</span>
						<input type="password" class="input-text input-max new-pwd" />
						<em class="tips-box"></em>
					</label>
					<label>
						<span>确认新的登录密码：</span>
						<input type="password" class="input-text input-max confirm-pwd" />
						<em class="tips-box"></em>
					</label>
					<label>
						<span></span>
						<a href="javascript:void(0);" class="btn-base btn-submit">确定</a>
					</label>
				</div>
				<div class="modify-pwd-suc">
					<p>修改成功，请牢记新的登录密码！</p>
					<#-- <a href="/login">重新登录</a> -->
				</div>
				<div class="modify-pwd-err">
					<p>修改失败，请重新编辑！</p>
					<span>重新编辑</span>
				</div>
			</div>
      	</@myModule>
    </div>
    <@footer/>
    <@copyright />
    <@cityChange />
    <div class="popup"></div>
    <@js />
    <script>
    	$(function () {
    	
    		// 添加侧边栏选中样式
			$(".bg-french-gray").find(".list li").eq(6).addClass("active");

			// 验证当前密码
			$(".old-pwd").on("blur", function () {
				var _this = $(this),
					pwd = _this.val();
				if (pwd != "") {
					verificationOldPwd(pwd, _this);
				}
			});

			function verificationOldPwd(pwd, _this) {
				$.ajax({
					url  : "/password/verify",
					type : "GET",
					async: false,
					dataType: "json",
					data: {
                    	oldPass : pwd
                    },
					success : function (data) {
						//console.log(data);
						//console.log(_this);
						if (data.code == 200) {
							_this.siblings(".tips-box").removeClass("tips-err").addClass("tips-suc").html("密码正确！");
						} else {
							_this.siblings(".tips-box").removeClass("tips-suc").addClass("tips-err").html("密码错误！");
						}
					}
				});
			}

			// 验证新密码
			$(".new-pwd").on("blur", function () {
				if ($(this).val() != "") {
					var newPass = /^[0-9a-zA-Z_]{6,20}$/;
					if(newPass.test($(this).val())) {
						$(this).siblings(".tips-box").removeClass("tips-err").addClass("tips-suc").html("密码正确！");
					} else {
						$(this).siblings(".tips-box").removeClass("tips-suc").addClass("tips-err").html("请输入6-20位的密码！");
					}
				} else {
					$(this).siblings(".tips-box").removeClass("tips-suc").addClass("tips-err").html("请输入新密码！");
				}
			});
			
			// 验证重复密码
			$(".confirm-pwd").on("blur", function () {
				if ($(this).val() != "") {
					var newPwd = $(".new-pwd").val(),
						confirmPwd = $(this).val();
					if (newPwd == confirmPwd) {
						$(this).siblings(".tips-box").removeClass("tips-err").addClass("tips-suc").html("密码正确！");
					} else {
						$(this).siblings(".tips-box").removeClass("tips-suc").addClass("tips-err").html("请输入相同密码！");
					}
				} else {
					$(this).siblings(".tips-box").removeClass("tips-suc").addClass("tips-err").html("请再次输入新密码！");
				}
			});

			// 回退修改密码
			$(".modify-pwd-err span").on("click", function () {
				$(".modify-pwd-err").hide();
				$(".content").show();
			});

			// 修改密码
			$(".btn-submit").on("click", function () {
				var oldPass = $(".old-pwd").val(),
					newPass = $(".new-pwd").val(),
					confirmPass = $(".confirm-pwd").val();
				modifyPwd(oldPass, newPass, confirmPass);
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
						//console.log(data);
						if (data.success == 1) {
							$(".modify-pwd-suc").show();
							$(".content").hide();
						} else {
							$(".modify-pwd-err").show();
							$(".content").hide();
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