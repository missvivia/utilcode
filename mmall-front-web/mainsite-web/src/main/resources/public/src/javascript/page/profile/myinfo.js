/*
 * ------------------------------------------
 * 基本资料页面模块实现文件
 * @version  1.0
 * @author   zff(hzzhengff@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'util/form/form',
    'util/ajax/xdr',
    'util/encode/json',
    'pro/components/notify/notify',
    'pro/widget/module',
    'pro/widget/util/dateselect',
    'pro/widget/layer/phonebind/phonebind'
],function(_k,_e,_v,_u,Form,_j,JSON,notify,_m,DateSelect,PhoneWin,_p,_o,_f,_r){
	var _mobileReg = /^1[3|5|7|8|][0-9]{9}$/,
	   //_emailReg = /.\w*@.*\.\w/ig ,
	    _emailReg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/,
	    _findParent = function(_elm,_className){
			while(_elm&&!_e._$hasClassName(_elm,_className)){
				_elm = _elm.parentNode;
			}
			return _elm;
		};
    /**
     * 页面模块基类
     * 
     * @class   _$$Module
     * @extends _$$Module
     */
	var _pro;
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // TODO
        this.__getNodes();
        this.__addEvent();
        
    	this.__defaultBirthday = '1980-01-01'; 
    	var oldyear = this.__oldyear.value,
    		oldmonth = this.__oldmonth.value,
    		oldday = this.__oldday.value;
    	if(oldyear!='' && oldyear!=0 && oldmonth!='' && oldmonth!=0 && oldday!='' && oldday!=0){
    		this.__defaultBirthday = oldyear+'-'+oldmonth+'-'+oldday;
    	}
    	
//    	var _phone = this.__iptPhone0.value;
//    	if(_phone!=''){
//    		this.__txtPhone.innerText = _phone.substring(0,3)+'****'+_phone.substring(7);
//    	}
    	
    	this.__initWidget();
    };
    _pro.__getNodes = function(){
    	var _ntmp = _e._$getByClassName(document.body,'ztag'), _index = 0;
    	this.__oldyear = _ntmp[_index++];
    	this.__oldmonth = _ntmp[_index++];
    	this.__oldday = _ntmp[_index++];
    	this.__nickName = _ntmp[_index++];
    	this.__year = _ntmp[_index++];
    	this.__month = _ntmp[_index++];
    	this.__day = _ntmp[_index++];
//    	this.__birthday = _ntmp[_index++];
    	this.__gender_F = _ntmp[_index++];
    	this.__gender_M = _ntmp[_index++];
    	this.__email = _ntmp[_index++];
    	this.__saveBtn = _e._$get('saveBtn');
//    	this.__userErrmsg = _ntmp[_index++];
//    	this.__userSucmsg = _ntmp[_index++];
   // 	this.__bindBtn = _e._$get('bindBtn');
   // 	this.__txtPhone = _e._$get('txtphone');
    	this.__iptPhone0 = _e._$get('iptphone0');
    	this.__phoneErrmsg = _e._$get('phoneErrmsg');
    };
    _pro.__addEvent = function(){
    	_v._$addEvent(this.__saveBtn,'click',this.__onSaveBtnClick._$bind(this));
    	/*if(!!this.__bindBtn){
    		_v._$addEvent(this.__bindBtn,'click',this.__onBindBtnClick._$bind(this));
    	}*/
    };
    _pro.__initWidget = function(){
    	// 日期选择控件，year,month,day是select节点,date是初始值	
    	this.__dateselect = DateSelect._$$DateSelect._$allocate({
    		year:this.__year,
    		month:this.__month,
    		day:this.__day,
    		date:this.__defaultBirthday
    	});
    	// webForms
    	this.__form1 = Form._$$WebForm._$allocate({
    		form: _e._$get('profileform'),
    		attr: {
    			phone: function(target){
    				if(target.name=='phone'){
		                var phoneValue = _e._$get('iptphone0').value.trim();
		                if(!_mobileReg.test(phoneValue)){
		                    return 1;
		                }else{
		                	_e._$removeByEC(this.__phoneError);
							return null;
		                }
		            }
	            }._$bind(this),
	            email: function(target){
		            if(target.name=='email'){
		                var emailValue = _e._$get('email').value.trim();
		                if(emailValue != '' && !_emailReg.test(emailValue)){
		                    return 2;
		                }else{
		                	_e._$removeByEC(this.__emailError);
							return null;
		                }
		            }
		        }._$bind(this)
			}
    	});
    };
    _pro.__onBindBtnClick = function(_event){
    	_v._$stop(_event);
    	PhoneWin._$allocate({
    		data: {phonenum: window['g_defaultAddressPhone']||'', txtphone:this.__txtPhone, iptphone0:this.__iptPhone0, phoneErrmsg:this.__phoneErrmsg}
    	})._$show();
    };
    _pro.__onSaveBtnClick = function(_event){
    	_v._$stop(_event);
   // 	if(this.__isLoading0) return;
   // 	this.__isLoading0 =  true;
    	if(this.__form1._$checkValidity()){
//    		this.__birthday.value = this.__dateselect._$getValue();
    		/*var data = this.__form1._$data(),
    		 
    		if(data.email){
    				if(!(/.\w*@.*\.\w/ig).test(data.email)){
						notify.notify({type: "error", message: "请输入正确的邮箱地址！"});
						this.__isLoading0 = false;
						return;
					}
    		}*/
    		// dwr sending & callback
    		this.__doPostSaveDWR0(this.__form1._$data());
    		//to do removed 3:
//    		console.log(this.__form1._$data());
//    		alert('基本信息保存成功！');
//    		this.__isLoading0 = false;
    	}
    };
    
    _pro.__doPostSaveDWR0 = function(data){
        _j._$request('/profile/saveProfile', {
        	headers:{"Content-Type":"application/json;charset=UTF-8"},
			type:'json',
		    method:'POST',
		    data:JSON.stringify(data),
            onload: function(_data){             // 见上面的cbSave函数
               // 正常回调处理
               if(_data.code==200){
              	  // 成功              
//            	  this.__userErrmsg.style.display = 'none';
//           	  this.__userSucmsg.style.display = '';
            	 // notify.notify({type: "success", message: "基本信息保存成功！"});
            	  $.message.alert("基本信息保存成功！", "success");
            	  this.__refreshTopbar();
               }else{                             
              	  //失败
//            	  this.__userErrmsg.style.display = '';
//           	  this.__userSucmsg.style.display = 'none';
            	  //notify.notify({type: "error", message: "基本信息保存失败！"});
            	   $.message.alert(_data.message, "fail");
               }
             //  this.__isLoading0 = false;
            }._$bind(this),
            onerror:function(_error){
               // 异常处理
//             alert('基本信息保存失败，请稍后再试！');
//             this.__userErrmsg.style.display = 'none';
//       	   this.__userSucmsg.style.display = 'none';
//               notify.notify({type: "error", message: "基本信息保存失败，请刷新页面重试！"});
               $.message.alert("基本信息保存失败，请刷新页面重试！", "fail");
       		  // this.__isLoading0 = false;
            }._$bind(this),
            onbeforerequest:function(_data){
               // 请求发送前，对请求数据处理
            }
        });
    };
    
    // 设置side选中状态
    _pro.__setActive = function (classs) {
    	 var sideList = document.getElementById("m-side").getElementsByTagName("li");
    	 for ( var i = 0; i < sideList.length; i++) {
    		 if (sideList[i].getAttribute("class") == classs) {
    			 sideList[i].className = sideList[i].className + " active";
    		 }
    	 }
    };
    _pro.__setActive("info");
    
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
            countDown($(this), 45);
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
                //console.log(data);
                if (data.code == 200) {
                    $(".mobile-tips").addClass("tips-suc").removeClass("tips-err").html("验证码已发送，15分钟内输入有效！").show();
                } else {
                    $(".mobile-tips").addClass("tips-err").removeClass("tips-suc").html("发送失败!").show();
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
                if (data.code == 200) {
                    $(".popup").empty();
                    window.location.href = "/profile/basicinfo";
                } else {
                    $(".mobile-tips").addClass("tips-err").removeClass("tips-suc").html("校验码失效，请重新获取！").show();
                }
            }
        });
    }

    
    // init page
    _p._$$Module._$allocate();
});