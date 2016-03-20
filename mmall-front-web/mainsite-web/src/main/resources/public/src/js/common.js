/*
    *
    * @author E盘 http://fuzhongkuo.com
    *
**/

/**
	*
	* @ 全局变量
	*
**/
var windowH = $(window).height(),
	windowW = $(window).width(),
	bodyH = $("body").height(),
	bdoyW = $("body").width();



/**
 	*
 	* @ 回到顶部
 	*
 **/
$(window).scroll(function () {
    if ($(document).scrollTop() > 0) {
        $(".gotop").fadeIn();
    } else {
        $(".gotop").fadeOut();
    }
});
$.fn.gotop = function() {
	$(this).on("click", function() {
		$("body, html").animate({
			scrollTop: 0
		}, 400);
	});
};
$(".gotop").gotop();

/**
 	*
 	* @ input清空默认值 得失焦点状态
 	*
 **/
$.fn.inputClear = function() {
	$(this).on("focus", function() {
		$(this).addClass("input-focus");
		$(this).val(" ");
	});
	$(this).on("blur", function() {
		$(this).removeClass("input-focus");
		var defaultVal = this.defaultValue,
			val = $(this).val();
		if (val == defaultVal || val == " ") {
			$(this).val(defaultVal);
		} else {
			$(this).val(val);
		}
	});
};
$(".input-clear").inputClear();


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
        	$(btn).addClass("btn-disable").attr("disabled", true).val("剩余时间" + "(" + countDownTime + ")");
            i++;
        }
    }, 1000);
}

/**
	*
 	* @  隔行变色
 	*
**/
$.fn.tableUi = function() {
    var defaults = {
        odd: "bg-odd",
        even: "bg-even",
        selected: "bg-selected"
    };
    var _this = $(this);
    _this.find("tr:odd").addClass(defaults.odd);
    _this.find("tr:even").addClass(defaults.even);
    _this.find("tr").hover( function () {
        $(this).addClass(defaults.selected);
    }, function () {
        $(this).removeClass(defaults.selected);
    });
};
$(".table-ui tbody").tableUi();

/**
	*
 	* @  全选
 	*
**/
$.fn.checkAll = function () {
	$(this).on("click", function () {
		if($(this).attr("checked")) {
			$(".check-all-list").find("input[type='checkbox']").removeAttr("checked");
		   	$(this).removeAttr("checked");
		} else {
			$(".check-all-list").find("input[type='checkbox']").prop("checked",'true');
		    $(this).attr("checked",'true');
		}
	});
};
$(".check-all").checkAll();

/**
	*
 	* @  弹出层 参数tit 为设置弹窗标题 参数width 为设置给弹窗的宽度类名
 	*
**/
function createPopup(width) {
	var popupDom = "";
	popupDom += "<div class='opacity'></div>"+
				"<div class='box  "+ width +"'>"+
					"<div class='title'>"+
						"<b></b>"+
						"<span class='close'>X</span>"+
					"</div>"+
					"<div class='content'></div>"+
				"</div>";
	$(".popup").append(popupDom);
}

$.fn.popup = function (tit) {
	var _this = $(this),
		windowH = $(window).height(),
		bodyH = $("body").height(),
		bdoyW = $("body").width(),
		opacity = _this.find(".opacity"),
		box = _this.find(".box"),
		boxH = box.height(),
		boxW = box.width();

	opacity.css({
		width : bdoyW,
		height : bodyH
	});
	box.css({
		left : (bdoyW - boxW)    / 2,
		top  : (windowH - boxH)  / 2
	});
	_this.find(".title b").html(tit);
}

$(".popup").on("click", ".close", function () {
	$(".popup").empty();
});


// 登录框
function login() {
	createPopup("min-box");
    $(".popup").popup("登录");
	var dom = "";
	dom += "<div class='login-window'>"+
				"<input type='text' class='input-text input-max' value='邮箱/手机号/用户名' />"+
				"<input type='password' class='input-text input-max' />"+
				"<input type='button' value='登录' class='login-submit' />"+
				"<p class='clearfix'>"+
					"<a href='#' class='l forget-pwd'>忘记密码</a>"+
					"<a href='#' class='r go-register'>免费注册</a>"+
				"</p>"+
				"<p class='other-login'>第三方登录</p>"+
				"<p>"+
					"<a href='#' class='wy-login'></a>"+
					"<a href='#' class='qq-login'></a>"+
					"<a href='#' class='wb-login'></a>"+
				"</p>"+
			"</div>";
	$(".popup").find(".content").append(dom);
}

/**
	*
	* @ 根据ip获取当前城市
	*
**/
function getCurrentCity() {
	$.ajax({
		type : "GET",
		url: " /location/currentArea",
		async: false,
		contentType: "application/json",
		data :{
		},
		success: function (data) {
			if (data.code == 200) {
			}
		}
	});
}
//getCurrentCity();

// 获取cookie
function getCookie(obj) {
	var cookieName = obj,
		cookieStar = "",
		cookieEnd = "",
		cookieVal = document.cookie;

	// 判断是否有cookie
	if (cookieVal.length > 0) {
		// 判断cookie内是否包含指定参数
		cookieStar = cookieVal.indexOf(cookieName + "=");
		if (cookieStar >= 0) {
			// 从参数返回的值开始，判断后面字符串是否含有“;”
			cookieEnd = cookieVal.indexOf(";", cookieStar);
			if (cookieEnd >= 0) {
				cookieEnd = cookieEnd;
			} else {
				cookieEnd = cookieVal.length;
			}
			return decodeURIComponent(cookieVal.substring(cookieStar + 1 + cookieName.length, cookieEnd));
		} else {
			return null;
		}
	}
}


// 根据cookie获取 城市名 城市ID 区
var locationCityName = "",
	locationCityID = "",
	locationAreaName = "",
	cityNameEnd = 0,
	cityIDEnd = 0,
	areaNameEnd = 0,
	obj = getCookie("MMALL_AREA");

cityNameEnd = obj.indexOf("|");
locationCityName = obj.substring(0, cityNameEnd);

cityIDEnd = obj.indexOf("|", cityNameEnd + 1);
locationCityID = obj.substring(cityNameEnd + 1, cityIDEnd);

areaNameEnd = obj.lastIndexOf("|");
locationAreaName = obj.substring(cityIDEnd + 1, areaNameEnd);

areaIDEnd = obj.indexOf("#");
locationAreaID = obj.substring(areaNameEnd + 1, areaIDEnd);

/**
	*
	* @ 顶部条
	*
**/

// 顶部bar
var currentState = $(window).scrollTop() < 666,
	fixedTop = $(".fixed-top");
$(window).scroll(function(){
	var state = $(window).scrollTop() < 666;
	if (state != currentState) {
		if(state) {
			fixedTop.find(".category-nav").hide();
	    	fixedTop.find(".category-side").hide();
	    	fixedTop.find("dt").removeClass();
	        fixedTop.animate({top : -45}, 100);
		} else {
		fixedTop.animate({top : 0}, 100);
	}
	currentState = state ;
	}
})

// 顶部下拉菜单
$(function () {
	$(".pull-down").hover( function () {
		$(this).toggleClass("active").find("dd").show();
	}, function () {
		$(this).toggleClass("active").find("dd").hide();
	});
});

// 设置bartop 当前区域名称 添加城市di
$(".region-select em").html(locationCityName + "(" + locationAreaName + ")").attr("data-code", locationCityID);

// 城市获取切换
var districtBox = $(".current-area .ars"),
	districtDom = "",
	cityInOrderBox = $(".city-change .other-city .common"),
	cityInOrderDom = "",
	cities = {},
	areaFloatBox = $(".city-change .area-float"),
    uparrow2 = $(".city-change .uparrow2");

// 弹出选择城市对话框
$(".region-select em").click( function() {
	changeCity();
	$(".city-change .tit span em").html(locationCityName).attr("data-code", locationCityID);
	createDistrict(locationCityID, 0);
});

// 城市选择
function changeCity() {
	var opacity = $(".city-change .opacity"),
		box = $(".city-change .content"),
		boxH = box.height(),
		boxW = box.width();
	$(".city-change").show();
	opacity.css({
		width : bdoyW,
		height : bodyH
	});
	box.css({
		left : (bdoyW - boxW)    / 2,
		top  : (windowH - 500)  / 2
	});
}
$(".city-change .close").on("click", function () {
	$(".city-change").hide();
});


//城市title切换
$(".city-change .tit dd").on("click", function(){
	areaFloatBox.hide();
	uparrow2.hide();
	$(this).siblings().find(".uparrow").hide();
	$(this).addClass("active").siblings().removeClass("active");
	$(this).find(".uparrow").show();
	if($(this).hasClass("cur")){
		$(".con .current-area").show();
		$(".con .other-city").hide();
	}else{
		$(".con .other-city").show();
		$(".con .current-area").hide();
	}
	if ($(this).hasClass("oth")) {
		getCityInOrder();
	}
});

//选择区
$(".city-change .current-area .ars, .city-change .area-float").on("click", "span", function(){
	$("this").addClass("selected").siblings().removeClass("selected");
	var areaCode = $(this).data("code");
	setChangeCityName(locationCityID, locationCityName);
	setArea(locationCityID, areaCode);
});

//设置城市区
function setArea(cityCode, areaCode) {
	$.ajax({
		type : "GET",
		url: '/section/s',
		async: false,
		dataType: "json",
		data: {
			c : cityCode,
			s : areaCode
	    },
	    success: function(data) {
	    	if (data.result) {
	    		$(".city-change").hide();
	    		window.location.href = "/";
	    	}
	    }
	});
}

//按照市首字母排序获取所有市
function getCityInOrder() {
	$.ajax({
		type : "GET",
		url: '/location/cityInOrder',
		async: false,
		dataType: "json",
		data: {
	    },
	    success: function(data) {
	    	if (data.code == 200) {
	    		cities = data.result.list;
	    	}
	    }
	});
}

//按字母切换城市
$(".other-city .tab dd").on("click", function(){
	$(this).addClass("active").siblings().removeClass("active");
	areaFloatBox.hide();
	uparrow2.hide();
	if($(this).hasClass("hot-tab")){
		$(".other-city .hot").show();
		$(".other-city .common").hide();
	}else{
		$(".other-city .hot").hide();
		$(".other-city .common").show();
		var name = $(this).html();
		if($(this).hasClass("y-z")){
			var nameArr = name.split("·").join("").split("");
		}else{
			var nameArr = name.split("");
		}
		cityInOrderBox.empty();
		for(var i = 0, length = nameArr.length; i<length; i++){
			createCityInOrder(nameArr[i]);
		}
	}
});

//按照首字母创建市列表
function createCityInOrder(name){
	var cityDom = "<div class='dist'><p>" + name + "</p><div class='cities'>";
	for(var i = 0; i < cities.length; i++){
		if(cities[i].key == name){
			var value = cities[i].value;
			if ( value.length != 0) {
				for(var j = 0; j < value.length; j++){
					cityDom += "<span data-code='"+ value[j].id +"' onclick='clickCity(this);'>"+ value[j].name +"</span>";
				}
			} else {
				cityDom += "<span>暂无城市</span>";
			}
		}
	}
	cityDom += "&nbsp;</div></div>";
	cityInOrderBox.append(cityDom);
}

//城市点击
$(".city-change .hot span").on("click", function () {
	clickCity(this);
});

//显示区
function clickCity(obj){
	var code = $(obj).data("code"),
		name = $(obj).html(),
		position = $(obj).offset(),
		height = $(obj).height(),
		width = $(obj).width(),
		arrowH = uparrow2.height();
	$(obj).addClass("selected").siblings().removeClass();
	locationCityID = code;
	locationCityName = name;
	createDistrict(code, 1);
	uparrow2.css({"top": position.top + height, "left": position.left + width/2 + 5}).show();
	areaFloatBox.css({"top": position.top + height + arrowH}).show();
}

//设置选中城市名
function setChangeCityName(code, name) {
	$(".region-select em").html(name).attr("data-code", code);
	$(".city-change .tit .cur em").html(name).attr("data-code", code);
}

//隐藏区
areaFloatBox.on("mouseleave", function () {
	$(this).hide();
	uparrow2.hide();
});

//根据城市code 获取区
function createDistrict(id, flag) {
	/*if(id != "-5001"){
		districtBox.append("<p style='text-align:center;padding-top:45px;'>该地区暂未开放，请选择其他城市</p>");
		return;
	}*/
	$.ajax({
		type : "GET",
		url: '/location/district',
		async: false,
		dataType: "json",
		data: {
			code : id
	    },
	    success: function(data) {
			if (data.code == 200) {
				//districtBox.empty();
				areaFloatBox.empty();
				districtDom = "";
				for (var i = 0; i < data.result.list.length; i++) {
					districtDom += "<span data-code="+ data.result.list[i].id +">"+ data.result.list[i].name +"</span>";
				}
				//districtBox.append(districtDom);
				areaFloatBox.append(districtDom);
			}
	    }
	});
}
/******************************************************************** E 顶部条 *******************************************************************/

/**
	* @获取购物车数量
**/
var cartNum = $(".js-cart-num");
function getCartNum() {
	if(cartNum.length > 0){
		$.ajax({
			cache: false,
			type : "GET",
			url: " /cart/getcount",
			async: false,
			dataType: "json",
			data : {},
			success: function (data) {
				if (data.code == 200) {
					cartNum.html(data.result);
				} else {
					return;
				}
			}
		});
	}
}
/******************************************************************** E 获取购物车数量 *******************************************************************/

/**
	* @判断用户是否登陆
**/
function isLogin() {
	var username = getCookie("XYLUN"),
		isLoginBox = $(".is-login"),
		noLoginBox = $(".no-login"),
		nameValBox = $(".username"),
		loginOut = $(".loginout"),
		flag = false;

	if (username != null ) {
		// 登陆成功返回首页，通过cookie获取不到nickName,通过接口获取！
		getCartNum();
		var nickName = getCookie("userNickName");
		if (nickName == null) {
			$.ajax({
				cache: false,
				async: false,
				type : "GET",
				url: " /profile/getUserInfo",
				dataType: "json",
				data : {},
				success: function (data) {
					if (data.code == undefined) {
						loginOut.remove();
						isLoginBox.hide();
						noLoginBox.show();
					} else {
						if (data.code == 200) {
							nickName = data.result.nickname;
							isLoginBox.show();
							noLoginBox.hide();
							nameValBox.html(nickName);
							$(".pull-down").show();
						} else {
							loginOut.remove();
							isLoginBox.hide();
							noLoginBox.show();
						}
					}
				}
			});
		} else {
			isLoginBox.show();
			noLoginBox.hide();
			nameValBox.html(nickName);
			$(".pull-down").show();
		}
	} else {
		loginOut.remove();
		isLoginBox.hide();
		noLoginBox.show();
	}
}
isLogin();
/******************************************************************** E 判断用户是否登陆 *******************************************************************/

/**
	* @用户退出登陆
**/
$(".loginout").on("click", function () {
	var url = window.location.href;
	$.ajax({
		url : "/logout",
		type : "POST",
		data : {},
		success : function (data) {
			window.location.href = "/login?redirectURL="+encodeURIComponent(url);;
		}
	});
});
/******************************************************************** E 用户退出登陆 *******************************************************************/

//加法函数，用来得到精确的加法结果
//说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
//调用：accAdd(arg1,arg2)
//返回值：arg1加上arg2的精确结果
function accAdd(arg1,arg2){
    var r1,r2,m;
    try{
    	r1 = arg1.toString().split(".")[1].length;
    }catch(e){
    	r1 = 0;
    }
    try{
    	r2 = arg2.toString().split(".")[1].length;
    }catch(e){
    	r2 = 0;
    }
    m = Math.pow(10,Math.max(r1,r2));
    return (arg1*m + arg2*m)/m;
}
//给Number类型增加一个add方法，调用起来更加方便。
Number.prototype.add = function (arg){
    return accAdd(arg,this);
}

//减法函数，用来得到精确的减法结果
//说明：javascript的减法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的减法结果。
//调用：accSubtr(arg1,arg2)
//返回值：arg1减去arg2的精确结果
function accSubtr(arg1,arg2){
	var r1,r2,m,n;
	try{
		r1=arg1.toString().split(".")[1].length;
	}catch(e){
		r1=0;
	}
	try{
		r2=arg2.toString().split(".")[1].length;
	}catch(e){
		r2=0;
	}
	m=Math.pow(10,Math.max(r1,r2));
	//动态控制精度长度
	//n=(r1>=r2)?r1:r2;
	return ((arg1*m-arg2*m)/m).toFixed(2);
}
//给Number类型增加一个subtr 方法，调用起来更加方便。
Number.prototype.subtr = function (arg){
	return accSubtr(this, arg);
}

//乘法函数，用来得到精确的乘法结果
//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
//调用：accMul(arg1,arg2)
//返回值：arg1乘以arg2的精确结果
function accMul(arg1,arg2){
	var m = 0,
	    s1 = arg1.toString(),
	    s2 = arg2.toString();
	try{
		m += s1.split(".")[1].length;
	}catch(e){}
	try{
		m += s2.split(".")[1].length;
	}catch(e){}
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
}
//给Number类型增加一个mul方法，调用起来更加方便。
Number.prototype.mul = function (arg){
	return accMul(arg, this);
}

//除法函数，用来得到精确的除法结果
//说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
//调用：accDiv(arg1,arg2)
//返回值：arg1除以arg2的精确结果

function accDiv(arg1,arg2){
	var t1=0,t2=0,r1,r2;
	try{
		t1 = arg1.toString().split(".")[1].length;
	}catch(e){}
	try{
		t2 = arg2.toString().split(".")[1].length;
	}catch(e){}
	with(Math){
		r1 = Number(arg1.toString().replace(".",""))
		r2 = Number(arg2.toString().replace(".",""))
		return ((r1/r2)*pow(10,t2-t1));
	}
}
//给Number类型增加一个div方法，调用起来更加方便。
Number.prototype.accDiv = function (arg){
    return accDiv(this, arg);
}
/******************************************************************** E 重写浮点运算的函数，解决js浮点数运算bug *******************************************************************/
/**
    *
	* @  提示框
	*
**/
;(function($){

// 创建基本提示框
function createMessage(content, buttons){
	var message = $("<div class='messagetip'></div>").appendTo("body");
	message.append("<div class='tiptop'><span class='close'></span></div><div class='tipinfo'>"+content+"</div>");
	if(buttons){
		var buttonDiv = $('<div class="tipbtn"></div>').appendTo(message);
		for(var label in buttons){
			if(label=="sure"){
				$('<input type="button" class = "sure message-btn" value="确定"/>')
				 .bind("click",buttons[label]).appendTo(buttonDiv);
			}else{
				$('<input type="button" class = "cancel message-btn" value="取消"/>')
				 .bind("click",buttons[label]).appendTo(buttonDiv);
			}
		}
		buttonDiv.wrap("<div class='tipbuttom'></div>");
	}
	 message.find(".tiptop span").click(function(){
      	close(message);
     });
     return message;
}

// 打开提示框
function open(message){
    var data = message.data("message");
   	data.message.fadeIn(0);
    data.mask.css('display', 'block');
}

// 关闭提示框
function close(message){
	if(message.data("message")){
		var data = message.data("message");
    	data.message.remove();
	    data.mask.remove();
    }
}

// 创建遮罩层
function createMask(message){
	if (message.data('message').mask) {
		message.data('message').mask.remove();
	}

    message.data('message').mask = $('<div class="messageMask"></div>')
		.css({
			  zIndex: $.message.defaults.zIndex++,
			  width: $(document).width(),
			  height: $(document).height(),
		      left: 0,
		      top: 0
			  })
		.appendTo($(document.body));
}
/**
    *
	* @ 提示框入口函数
    *   alert  用于普通的提示信息
    *   调用方式：$.message.alert("请选择一条记录", "success");
	*   参数msg 为提示信息  参数icon 为logo样式（三种：成功logo："success", 失败logo："fail", 一般提示信息logo："info"）
	*   参数fn  为点击确定按钮后执行的回调函数，不需向此函数传递参数  没有回调函数可以不写
	*   用法：$.message.alert("请填写详细地址！", "info", function(){
                                $(".address-info").focus();
                           });
	*   参数para  可设置弹出框的宽度等，通常情况下不用写此参数
    *
    *   confirm  用于删除等确认信息
    *   调用方式：$.message.confirm("确认要删除吗", "del",  function(data){
    *	                                 alert(data);
    *							});
    *   参数msg 为提示信息  参数icon 为logo样式（两种：删除logo："del", 一般确认信息logo："info"）
    *   参数fn 为回调函数，当点击确定时，会返回true给此函数，点击取消，返回false给此函数
    *   参数para  可设置弹出框的宽度等，通常情况下不用写此参数
    *
**/
$.message = {
     alert: function(msg, icon, fn, para){
    	    var opts = null;
    	    var message = null;
			var windowH = $(window).height();
			var bodyH = $("body").height();
	        var bdoyW = $("body").width();
			opts = $.extend({}, $.message.defaults, para||{});
			var content = '<div>' + msg + '</div>';
			switch(icon) {
				case 'success':
					content = '<div class="messageIcon messageSuccess"></div>' + content;
					break;
				case 'fail':
					content = '<div class="messageIcon messageFail"></div>' + content;
					break;
				case 'info':
					content = '<div class="messageIcon messageInfo"></div>' + content;
					break;
			}

			var buttons = {};
			buttons[$.message.defaults.ok] = function(){
				close(message);
				if (fn){
				      fn();
				      return false;
			    }
			};

			var message = createMessage(content, buttons);
    	    message.data("message", {
            	message:message
            });

    	    message.width(opts.width);
    	    message.find(".tipbuttom").width(opts.width);
			var mHeight = message.height();
    	    message.css({
				"left":(bdoyW - opts.width) / 2,
			    "top":(windowH - mHeight) / 2
		    });

			createMask(message);

            message.css('z-index', $.message.defaults.zIndex++);
            open(message);

		},

		confirm: function(msg, icon, fn, para) {
			var opts = null;
			var message = null;
			var windowH = $(window).height();
			var bodyH = $("body").height();
	        var bdoyW = $("body").width();
			opts = $.extend({}, $.message.defaults, para||{});
			var content = '<div>' + msg + '</div>';
			switch(icon) {
				case 'info':
					content = '<div class="messageIcon messageInfo"></div>' + content;
					break;
				case 'del':
					content = '<div class="messageIcon messageDel"></div>' + content;
					break;
			}

		    var buttons = {};
		    buttons[$.message.defaults.ok] = function(){
			      close(message);
			      if (fn){
				      fn(true);
				      return false;
			      }
		    };
		    buttons[$.message.defaults.cancel] = function(){
		    	 close(message);
			     if (fn){
				     fn(false);
				     return false;
			     }
		    };
			var message = createMessage(content, buttons);
    	    message.data("message",{
            	message:message
            });

    	    message.width(opts.width);
    	    message.find(".tipbuttom").width(opts.width);
			var mHeight = message.height();
    	    message.css({
				"left":(bdoyW - opts.width) / 2,
			    "top":(windowH - mHeight) / 2
		    });

        	createMask(message);

            message.css('z-index', $.message.defaults.zIndex++);
            open(message);
       }
};

// 提示框默认参数    width为弹出框的宽度   zIndex为z轴方向的层级   ok为确定按钮样式    cancel为取消按钮样式
$.message.defaults={
	width: 350,
	zIndex: 11,
	ok: 'sure',
	cancel: 'cancel'
};

})(jQuery);
/******************************************************************** E 提示框 *******************************************************************/

/*
	* 缩略图参数
	* @ q = 质量  w = 宽度 h = 高度
*/
function thumbnailParam(q, w, h) {
	var param = "?imageView&quality="+q+"&thumbnail="+w+"x"+h;
	return param;
}