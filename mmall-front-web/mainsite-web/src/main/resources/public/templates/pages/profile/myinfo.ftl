<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
        <#include "/wrap/common.ftl">
        <#include "/wrap/my.common.ftl">
        <@title type="myInfo" />
        <@css/>
        <@cssCore />
    </head>
    <body>
        <@fixedTop />
        <@topbar />
        <@top />
        <@mainCategory />
        <div class="m-crumbs wrap">
            <a href="/">首页</a><span>&gt;</span><a href="/profile/basicinfo">个人中心</a><span>&gt;</span><span class="selected">个人资料</span>
        </div>
        <div class="m-wrap clearfix">
            <@mSide />
            <div class="m-main r">
                <div class="title">
                    <b>个人资料</b>
                </div>
                <div class="content">
                	<input type="hidden" value="${(profile.birthYear)?default('')}" name="oldyear" />
		    		<input type="hidden" value="${(profile.birthMonth)?default('')}" name="oldmonth"/>
		    		<input type="hidden" value="${(profile.birthDay)?default('')}" name="oldday" />
                    <table width="100%">
                        <tr>
                            <th width="10%">帐号：</th>
                            <td>${(profile.account)?default('')}</td>
                        </tr>
                        <tr>
                            <th><em class="red">*</em>昵称：</th>
                            <td>
                                <input type="text" class="input-text input-max nickname" value="${(profile.nickname)?default('')}" />
                            </td>
                        </tr>
                        <tr>
                            <th>生日：</th>
                            <td>
                                <select class="select birth-year">
                                </select>
                                <select class="select birth-month">
                                </select>
                                <select class="select birth-day">
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>性别：</th>
                            <td>
                                 <#if (profile.gender)?exists && (profile.gender)==1>
                                 	<label>
	                                    <input name="gender" type="radio" value="0" />男
	                                </label>
	                                <label>
	                                    <input name="gender" type="radio" value="1" checked="checked" />女
	                                </label>
				                 <#else>
				                     <label>
	                                    <input name="gender" type="radio" value="0" checked="checked" />男
	                                </label>
	                                <label>
	                                    <input name="gender" type="radio" value="1" />女
	                                </label>
				                 </#if>
                            </td>
                        </tr>
                        <tr>
                            <th><em class="red">*</em>手机：</th>
                            <td>
                                <span>${(profile.phone)?default('')}</span>
                                <span class="btn-base btn-submit modify-mobile-btn">修改手机号码</span>
                            </td>
                        </tr>
                        <tr>
                            <th>邮箱：</th>
                            <td>
                                <input type="text" class="input-text input-max email-val" value="${(profile.email)?default('')}" />
                            </td>
                        </tr>
                        <tr>
                            <th></th>
                            <td><input type="button" value="提交" class="btn-base btn-submit save" /></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <@footer/>
        <@copyright/>
        <@cityChange />
        <div class="popup"></div>
        <@js />
        <script type="text/javascript">
            $(function () {

                // 设置侧边栏选中
                $(".m-side li").eq(4).addClass("active");

                // 创建生日select列表
                var oldyear   = $("input[name='oldyear']").val(),
                    oldmonth  = $("input[name='oldmonth']").val(),
                    oldday    = $("input[name='oldday']").val(),
                    birthYear = $(".birth-year"),
                    birthMonth = $(".birth-month"),
                    birthDay = $(".birth-day"),
                    selectDom = "";

                function creatSelect(start, end, box) {
                    var len = end - start,
                        dom = "";
                    for (var i = 0; i < len + 1; i++) {
                        dom += "<option>"+ (Number(start) + i) +"</option>";
                    }
                    box.append(dom);
                }
                creatSelect("1958", "2015", birthYear);
                creatSelect("1", "12", birthMonth);
                creatSelect("1", "31", birthDay);

                birthYear.find("option").each(function () {
                    if (oldyear == $(this).val()) {
                        $(this).attr('selected','selected');
                    }
                });
                birthMonth.find("option").each(function () {
                    if (oldmonth == $(this).val()) {
                        $(this).attr('selected','selected');
                    }
                });
                birthDay.find("option").each(function () {
                    if (oldday == $(this).val()) {
                        $(this).attr('selected','selected');
                    }
                });

                // 修改手机密码
                var mobileReg = /^1[3|5|7|8|][0-9]{9}$/;
                $(".modify-mobile-btn").click(function () {
                    createPopup("min-box");
                    $(".popup").popup("更改手机号码");
                    createDom();
                });
                
                function createDom() {
                    var dom = "";
                    dom += "<div class='modify-mobile'>"+
                                "<div class='group'>"+
                                    "<input type='text' class='input-text mobile-code' placeholder='请输入手机号码' />"+
                                    "<input type='button' class='btn-base btn-cancel count-down' value='获取验证码' />"+
                                "</div>"+
                                "<div class='group'>"+
                                    "<input type='text' class='input-text verification-code' placeholder='6位数字验证码' />"+
                                "</div>"+
                                "<div class='group'>"+
                                    "<p class='mobile-tips'></p>"+
                                "</div>"+
                                "<div class='group'>"+
                                    "<span class='btn-base btn-submit'>确定</span><span class='btn-base btn-cancel btn-cancel-btn'>取消</span>"+
                                "</div>"+
                            "</div>";
                    $(".popup").find(".content").empty();
                    $(".popup").find(".content").append(dom);
                }

                $(".popup").on("click", ".count-down", function () {
                    var code = $(".mobile-code").val();
                    if (code == "") {
                        $(".mobile-tips").addClass("tips-err").removeClass("tips-suc").html("请输入手机号码！").show();
                    } else if (mobileReg.test(code) == false) {
                        $(".mobile-tips").addClass("tips-err").removeClass("tips-suc").html("请输入手正确机号码！").show();
                    } else {
                        getCode(code);
                    }
                });

                // 获取验证码
                function getCode(code) {
                    $.ajax({
                        url : "/profile/mobile/getCode",
                        type : "GET",
                        dataType : "json",
                        data : {
                            mobile : code
                        },
                        success : function (data) {
                            if (data.code == 200) {
                                countDown($(".count-down"), 45);
                                $(".mobile-tips").addClass("tips-suc").removeClass("tips-err").html("验证码已发送，15分钟内输入有效！").show();
                            } else {
                                $(".mobile-tips").addClass("tips-err").removeClass("tips-suc").html(data.message).show();
                            }
                        }
                    });  
                }
                
                // 绑定新号码
                $(".popup").on("click", ".btn-submit", function () {
                    var mobile = $(".mobile-code").val(),
                        code = $(".verification-code").val();
                    if (mobile == "" || code == "" || mobileReg.test(mobile) == false) {
                        $(".mobile-tips").addClass("tips-err").removeClass("tips-suc").html("请填写正确的手机号码和验证码！").show();
                    } else {
                        bindMobile(mobile, code);
                    }
                });

                $(".popup").on("click", ".btn-cancel-btn", function () {
                    $(".popup").empty();
                });

                function bindMobile(mobile, code) {
                    $.ajax({
                        url : "/profile/mobile/bind",
                        type : "GET",
                        dataType : "json",
                        data : {
                            mobile : mobile,
                            code : code
                        },
                        success : function (data) {
                            //console.log(data);
                            if (data.code == 200) {
                                $(".popup").empty();
                                window.location.href = "/profile/basicinfo";
                            } else {
                                $(".mobile-tips").addClass("tips-err").removeClass("tips-suc").html(data.message).show();
                            }
                        }
                    });
                }

                // 验证邮箱
                var emailReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                $(".email-val").on("blur", function () {
                    var val = $(this).val();
                    if (val != "") {
                        if (!emailReg.test(val)) {
                            $.message.alert("请输入正确的邮箱地址！", "info");
                        }
                    }
                });

                // 保存个人资料
                $(".save").on("click", function () {
                    var obj = {},
                        yearVal = $(".birth-year").val(),
                        monthVal = $(".birth-month").val(),
                        dayVal = $(".birth-day").val(),
                        genderVal = $("input[name='gender']:checked").val(),
                        emailVal = $(".email-val").val(),
                        nicknameVal = $(".nickname").val();

                    obj = {
                        birthDay: dayVal,
                        birthMonth: monthVal,
                        birthYear: yearVal,
                        email: emailVal,
                        gender: genderVal,
                        nickname: nicknameVal
                    };

                    if (nicknameVal == "") {
                        $.message.alert("昵称不能为空！", "info");
                    } else if (!emailReg.test($(".email-val").val()) && $(".email-val").val() != "") {
                        $.message.alert("请输入正确的邮箱地址！", "info");
                    } else {
                        saveProfile(obj);
                    }

                });
                function saveProfile(obj) {
                console.log(obj);
                    $.ajax({
                        url : "/profile/saveProfile",
                        type : "POST",
                        cache: false,
                        contentType: "application/json",
                        data : JSON.stringify({
                            birthDay: obj.birthDay,
                            birthMonth: obj.birthMonth,
                            birthYear: obj.birthYear,
                            email: obj.email,
                            gender: obj.gender,
                            nickname: obj.nickname
                        }),
                        success: function (data) {
                            if (data.code == 200) {
                                $.message.alert("保存成功！", "success", function () {
                                    window.location.href = "/profile/basicinfo";
                                });
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