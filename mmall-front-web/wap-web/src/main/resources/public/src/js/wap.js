/*
	* get view width set font-size
*/
function setFontSize() {
	var wrapWidth = $(".wrap").width(),
		fontSize = (wrapWidth / 640) * 50;
	$("html").css("font-size", fontSize + "px");
}
setFontSize();


/*
	* go back
*/
$.fn.goBack = function () {
	$(this).on("click", function () {
		window.history.go(-1);
	});
};
$(".go-back").goBack();


/*
	* go top
*/
$(window).scroll(function () {
    if ($(document).scrollTop() > 400) {
        $(".gotop").show();
    } else {
        $(".gotop").hide();
    }
});
$.fn.goTop = function () {
	$(this).on("click", function () {
		$("body, html").animate({
			scrollTop: 0
		}, 400);
	});
};
$(".gotop").goTop();

/*
	* set width
*/
$.fn.setWidth = function () {
	var wrapWidth = $(".wrap").width();
	$(this).css({
		width: wrapWidth + "px"
	});
};
$(".back-head").setWidth();
$(".fixed-foot").setWidth();

/*
   * clear input
 */
$(".ipt").on("keyup",function(){
    if($(this).val().length > 0){
        $(this).closest(".ipt-con").find(".clear").show();
    }else{
        $(this).closest(".ipt-con").find(".clear").hide();
    }
});

$(".clear").on("click", function(){
    $(this).closest(".ipt-con").find(".ipt").val("").focus();
    $(this).hide();
});

/*
	* list add cart
*/
$.fn.listAddCart = function () {
	var _this = $(this);
	_this.on("click", ".add-cart", function () {
		if ($(this).hasClass("disable")) {
			return;
		} else {
	        var skuId = $(this).closest("li").data("skuid"),
	            skuNum = $(this).closest("li").data("skunum");
	        if (skuNum > 0) {
	            addToCart(skuId);
	        } else {
	            $.message.alert("提示", "库存不足！");
	        }
        }
    });

    function addToCart(id) {
        $.ajax({
            type : "POST",
            url: "/cart/addToCart",
            async: false,
            contentType: "application/json",
            data : JSON.stringify({
                "skuId" : id,
                "diff" : 1
            }),
            success: function (data) {
                if (data.code == 200) {
                    $.message.alert("提示", "加入进货单成功！", function () {
                    	getCartNum();
                    });
                } else if(data.code == 403){
                    $.message.alert("提示", "您尚未登录，请先登录！", function(){
                        window.location.href = "/login?redirectURL="+encodeURIComponent(url);
                    });
                } else if(data.code == 404){
                    $.message.alert("提示", "进货单中最多只能加入50件不同的商品！");
                }
            }
        });
    }
};
$("#proList").listAddCart();

/*
	* get cart number
*/
function getCartNum() {
	var cartNum = $(".CartNum");
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

/*
	* into cart list page
*/
$.fn.intoCartList = function () {
	$(this).on("click", function () {
		window.location.href = "/cartlist";
	});
};
$(".back-head .cart").intoCartList();

/*
	* create popup layout
 */
function createPopup() {
	var wrapWidth = $(".wrap").width(),
		windowHeight = $(window).height(),
		dom = "<div class='popup'></div>";
	$(".wrap").append(dom);

	$("body").find(".popup").css({
		width: wrapWidth,
		height: windowHeight,
	});
}

/*
	* clear popup layout
*/
function clearPopup() {
	$("body").find(".popup").remove();
}

/*
	* null page
*/
function nullPage() {
	var windowHeight = $(window).height(),
		wrapWidth = $(".wrap").width(),
		headerHeight = $(".back-head").height();
	$("body").find(".null-page").css({
		height: windowHeight - headerHeight + "px"
		//width: wrapWidth - (wrapWidth * 0.12) + "px"
	});
}

/*
	* thumbnail param
	* @ q = quality  w = width  h = height
*/
function thumbnailParam(q, w, h) {
	return param = "?imageView&quality="+q+"&thumbnail="+w+"x"+h;
}

/*
   * reset
 */
$(window).resize(function() {
	setFontSize();
	// $(".back-head").setWidth();
	// $(".fixed-foot").setWidth();
});

/*
	* message
*/
;(function($){

	// 创建基本提示框
	function createMessage(title, content, buttons){
		var message = $("<div class='messagetip'></div>").appendTo("body");
		message.append("<div class='tiptop'>"+title+"</div><div class='tipinfo'>"+content+"</div>");
		if(buttons){
			var buttonDiv = $('<ul class="tipbtn"></ul>').appendTo(message);
			var flag = false;
			for(var label in buttons){
				if(label=="cancel"){
					$('<li class="part"><input type="button" class = "cancel message-btn" value="取消"/></li>')
					 .bind("click",buttons[label]).appendTo(buttonDiv);
					flag = true;
				}else{
					$('<li class="ok"><input type="button" class = "sure message-btn" value="确定"/></li>')
					 .bind("click",buttons[label]).appendTo(buttonDiv);
				}
				if(flag == true){
					$(buttonDiv).find(".ok").addClass("part br");
				}
			}
			buttonDiv.wrap("<div class='tipbuttom'></div>");
		}
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
	    *   调用方式：$.message.alert("提示","请选择一条记录");
		*   参数title为提示框标题；参数msg 为提示信息；
		*   参数fn  为点击确定按钮后执行的回调函数，不需向此函数传递参数  没有回调函数可以不写
		*   用法：$.message.alert("提示", "请填写详细地址！", function(){
	                                $(".address-info").focus();
	                           });
		*   参数para  可设置弹出框的宽度等，通常情况下不用写此参数
	    *
	    *   confirm  用于删除等确认信息
	    *   调用方式：$.message.confirm("提示", "确认要删除吗", function(data){
	    *	                                 alert(data);
	    *							});
	    *   参数title为提示框标题；参数msg 为提示信息；
	    *   参数fn 为回调函数，当点击确定时，会返回true给此函数，点击取消，返回false给此函数
	    *   参数para  可设置弹出框的宽度等，通常情况下不用写此参数
	    *
	**/
	$.message = {
	     alert: function(title, msg, fn, para){
	    	    var opts = null;
	    	    var message = null;
				var windowH = $(window).height();
				var bodyH = $("body").height();
		        var bdoyW = $("body").width();
				opts = $.extend({}, $.message.defaults, para||{});
				var content = '<div>' + msg + '</div>';

				var buttons = {};
				buttons[$.message.defaults.ok] = function(){
					close(message);
					if (fn){
					      fn();
					      return false;
				    }
				};

				message = createMessage(title, content, buttons);
	    	    message.data("message", {
	            	message:message
	            });

	    	    message.width(opts.width/50+"em");
	    	    message.find(".tipbuttom").width(message.width());
				var mHeight = message.height();
	    	    message.css({
					"left":(bdoyW - message.width()) / 2,
				    "top":(windowH - mHeight) / 2
			    });

				createMask(message);

	            message.css('z-index', $.message.defaults.zIndex++);
	            open(message);

			},

			confirm: function(title, msg, fn, para) {
				var opts = null;
				var message = null;
				var windowH = $(window).height();
				var bodyH = $("body").height();
		        var bdoyW = $("body").width();
				opts = $.extend({}, $.message.defaults, para||{});
				var content = '<div>' + msg + '</div>';

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
				message = createMessage(title, content, buttons);
	    	    message.data("message",{
	            	message:message
	            });

	    	    message.width(opts.width/50+"em");
	    	    message.find(".tipbuttom").width(message.width());
				var mHeight = message.height();
	    	    message.css({
					"left":(bdoyW - message.width()) / 2,
				    "top":(windowH - mHeight) / 2
			    });

	        	createMask(message);

	            message.css('z-index', $.message.defaults.zIndex++);
	            open(message);
	       },
			prompt: function(title, msg, fn, para) {
				var opts = null;
				var message = null;
				var windowH = $(window).height();
				var bodyH = $("body").height();
		        var bdoyW = $("body").width();
				opts = $.extend({}, $.message.defaults, para||{});
				var content = '<div>' + msg + '</div><div class="message-form"><input class="message-input" type="password"/></div>';

				var buttons = {};
			    buttons[$.message.defaults.ok] = function(){
				      if (fn){
				    	  var input = $(".message-input",message);
				    	  if(input.val()==""){
				    		  input.addClass("error");
				    		  input.focus();
				    		  return false;
				    	  }else{
				    		  close(message);
				    		  fn($(".message-input",message).val());
				    	  }

				      }
			    };
			    buttons[$.message.defaults.cancel] = function(){
			    	 close(message);
				     if (fn){
					     fn();
					     return false;
				     }
			    };
				message = createMessage(title, content, buttons);
	    	    message.data("message",{
	            	message:message
	            });

	    	    message.width(opts.width/50+"em");
	    	    message.find(".tipbuttom").width(message.width());
				var mHeight = message.height();
	    	    message.css({
					"left":(bdoyW - message.width()) / 2,
				    "top":(windowH - mHeight) / 2
			    });

	        	createMask(message);

	            message.css('z-index', $.message.defaults.zIndex++);
	            open(message);
	       },
	       addCoupon:function(title, fn, para) {
				var opts = null;
				var message = null;
				var windowH = $(window).height();
				var bodyH = $("body").height();
		        var bdoyW = $("body").width();
				opts = $.extend({}, $.message.defaults, para||{});
				var content = '<div class="message-form">'+
				                  '<input class="message-input coupon-code" type="text" placeholder="请输入优惠券码"/>'+
				                  /*'<div class="verify-group">'+
				                      '<input class="message-input verify-code" type="text" placeholder="请输入验证码"/>'+
				                      '<img src="/brand/genverifycode?"'+ Date.parse(new Date()) +' class="verify-img"/>'+
				                      '<i class="refresh-btn" onclick="refreshImg()"></i>'
				                  '</div>'+*/
				              '</div>';

				var buttons = {};
			    buttons[$.message.defaults.ok] = function(){
				      if (fn){
				    	  var couponCode = $(".coupon-code",message);
				    	   //   verifyCode = $(".verify-code",message);
				    	  if(couponCode.val()==""){
				    		  couponCode.focus().addClass("error");
				    		  return false;
				    	  }/*else if(verifyCode.val()==""){
				    		  couponCode.removeClass("error");
				    		  verifyCode.focus();
				    		  $(".verify-group").addClass("error");
				    	  }*/else{
				    		  couponCode.removeClass("error");
				    		 // $(".verify-group").removeClass("error");
				    		  close(message);
				    		  fn(couponCode.val());
				    	  }

				      }
			    };
			    buttons[$.message.defaults.cancel] = function(){
			    	 close(message);
				     if (fn){
					     fn();
					     return false;
				     }
			    };
				message = createMessage(title, content, buttons);
	    	    message.data("message",{
	            	message:message
	            });

	    	    message.width(opts.width/50+"em");
	    	    message.find(".tipbuttom").width(message.width());
				var mHeight = message.height();
	    	    message.css({
					"left":(bdoyW - message.width()) / 2,
				    "top":(windowH - mHeight) / 2
			    });

	        	createMask(message);

	            message.css('z-index', $.message.defaults.zIndex++);
	            open(message);
	       }
	};

	// 提示框默认参数    width为弹出框的宽度   zIndex为z轴方向的层级   ok为确定按钮样式    cancel为取消按钮样式
	$.message.defaults={
		width: 550,
		zIndex: 11,
		ok: 'sure',
		cancel: 'cancel'
	};

})(jQuery);