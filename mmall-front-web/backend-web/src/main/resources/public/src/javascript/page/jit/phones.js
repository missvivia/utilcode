/**
 * 商家平台-jit管理——短信接收号码绑定
 * author：hzzhengff(hzzhengff@corp.netease.com)
 */
define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'util/form/form'
        ,'{pro}widget/module.js'
        ,'util/encode/json'
        ,"{pro}components/notify/notify.js"
        ,'{lib}util/ajax/xdr.js'
        ,'{pro}components/jit/phoneactList.js'],
    function(ut,v,e,f,Module,JSON,notify,j,SizeList,p) {
        var pro;
        p._$$PhonesModule = NEJ.C();
        pro = p._$$PhonesModule._$extend(Module);
        
        var regExpPhone = /^1\d{10}$/;
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__actList = window["g_actList"]||[];
            this.form1 = f._$$WebForm._$allocate({form: e._$get('saveform1')});
            this.form2 = f._$$WebForm._$allocate({form: e._$get('saveform2')});
            this.form3 = f._$$WebForm._$allocate({form: e._$get('saveform3')});
            
            this.__getNodes();
            this.__addEvent();
            this.__initSizeComponent();
        };
        
        pro.__initSizeComponent = function(){
            if(this.__actList.length >0 ){
            	if(!this.__sizeList){
                    this.__sizeList = new SizeList({
                        data: {lists:this.__actList}
                    }).$inject("#size-list-box");
                }
            }
        };
        
        pro.__getNodes = function(){
        	var list = e._$getByClassName(document.body,'ztag'), i = 0;
        	this.credential1 = list[i++];
            this.expiredTime1 = list[i++];
            this.sign1 = list[i++];
            this.phonenum1 = list[i++];
            this.yzbtn1 = list[i++];
            this.yznum1 = list[i++];
            this.saveBtn1 = list[i++];
            this.mInfo1 = list[i++];
            
            this.credential2 = list[i++];
            this.expiredTime2 = list[i++];
            this.sign2 = list[i++];
            this.phonenum2 = list[i++];
            this.yzbtn2 = list[i++];
            this.yznum2 = list[i++];
            this.saveBtn2 = list[i++];
            this.mInfo2 = list[i++];
            
            this.credential3 = list[i++];
            this.expiredTime3 = list[i++];
            this.sign3 = list[i++];
            this.phonenum3 = list[i++];
            this.yzbtn3 = list[i++];
            this.yznum3 = list[i++];
            this.saveBtn3 = list[i++];
            this.mInfo3 = list[i++];
        };
        
        pro.__addEvent = function(){
        	v._$addEvent(this.yzbtn1,'click',this.__onValidateBtnClick._$bind(this));
        	v._$addEvent(this.yzbtn2,'click',this.__onValidateBtnClick2._$bind(this));
        	v._$addEvent(this.yzbtn3,'click',this.__onValidateBtnClick3._$bind(this));
        	v._$addEvent(this.saveBtn1,'click',this.__onSaveBtnClick._$bind(this));
        	v._$addEvent(this.saveBtn2,'click',this.__onSaveBtnClick2._$bind(this));
        	v._$addEvent(this.saveBtn3,'click',this.__onSaveBtnClick3._$bind(this));
        };
        
        pro.__onValidateBtnClick = function(event){
        	v._$stop(event);
        	
        	var _phonenum1 = this.phonenum1.value.trim();
        	//alert(_phonenum1);
        	
        	if(this.isSending1) return;
        	this.isSending1 =  true;
        	
        	var _hasError = false,
        		_errorMap = {};
        	if(!_phonenum1){
        		_errorMap['mustfilledin'] = '手机号码不能为空';
        		_hasError = true;
        	//}else if(isNaN(_phonenum1) || _phonenum1.length != 11){
        	}else if(!regExpPhone.test(_phonenum1)){
        		_errorMap['invalidate'] = '请填写正确的手机号码';
        		_hasError = true;
        	}
        	
        	if(_hasError){
        		this.isSending1 = false;
        		this.__showErrors(_errorMap,this.mInfo1);
        		return;
        	}
        	
        	//this.__showMsgs('发送中...','sending');  // 正在发送...
        	
        	// dwr sending & noneed callback function
        	this.__doPostVerifDWR(_phonenum1,1);
        	
			e._$addClassName(this.yzbtn1,'disabled');
			//e._$addClassName(this.saveBtn1,'disabled');
			this.time1 = 60;
			this.timer1 = window.setInterval(function(){
				this.__onShowLefTime(this.time1);
			}._$bind(this),1000);
        };
        pro.__onShowLefTime = function(i){
        	if(i>=10){
        		this.yzbtn1.innerText = '获取中(' + i + ')';
	    	}else{
	    		this.yzbtn1.innerText = '获取中(0' + i + ')';
	    	}
    		this.time1 = i - 1;
    		if(this.time1 < 0){
    			window.clearInterval(this.timer1);
    			e._$delClassName(this.yzbtn1,'disabled');
    			//e._$delClassName(this.saveBtn1,'disabled');
    			this.yzbtn1.innerText = '获取验证码';
    			this.isSending1 = false;
    		}
    	};
        pro.__onSaveBtnClick = function(event){
        	v._$stop(event);
        	
        	var _phonenum1 = this.phonenum1.value.trim(),
	        	_yznum1 = this.yznum1.value.trim();
        	//alert(_phonenum1+', '+_yznum1);
        	
        	//if(this.isLoading1||this.isSending1) return;
        	if(this.isLoading1) return;
        	this.isLoading1 =  true;
        	
        	var _hasError = false,
        		_errorMap = {};
        	if(!_phonenum1){
        		_errorMap['mustfilledin'] = '手机号码不能为空';
        		_hasError = true;
        	//}else if(isNaN(_phonenum1) || _phonenum1.length != 11){
        	}else if(!regExpPhone.test(_phonenum1)){
        		_errorMap['invalidate'] = '请填写正确的手机号码';
        		_hasError = true;
        	}else if(!_yznum1){
        		_errorMap['mustfilledin'] = '验证码不能为空';
        		_hasError = true;
        	}
        	
        	if(_hasError){
        		this.isLoading1 = false;
        		this.__showErrors(_errorMap,this.mInfo1);
        		return;
        	}
        	
        	//this.__showMsgs('保存中...','saving');  // 正在保存...
        	
        	// dwr sending & callback function
        	this.__doPostSaveDWR(this.form1._$data(),this.mInfo1,1);
        	
        	// to do remove 1:
        	//this.__showMsgs('保存成功',this.mInfo1);
        	//window.location.reload();
        	//this.isLoading1 = false;     
        };
        
        pro.__onValidateBtnClick2 = function(event){
        	v._$stop(event);
        	
        	var _phonenum2 = this.phonenum2.value.trim();
        	//alert(_phonenum2);
        	
        	if(this.isSending2) return;
        	this.isSending2 =  true;
        	
        	var _hasError = false,
        		_errorMap = {};
        	if(!_phonenum2){
        		_errorMap['mustfilledin'] = '手机号码不能为空';
        		_hasError = true;
        	//}else if(isNaN(_phonenum2) || _phonenum2.length != 11){
        	}else if(!regExpPhone.test(_phonenum2)){
        		_errorMap['invalidate'] = '请填写正确的手机号码';
        		_hasError = true;
        	}
        	
        	if(_hasError){
        		this.isSending2 = false;
        		this.__showErrors(_errorMap,this.mInfo2);
        		return;
        	}
        	
        	//this.__showMsgs('发送中...','sending');  // 正在发送...
        	
        	// dwr sending & noneed callback function
        	this.__doPostVerifDWR(_phonenum2,2);
        	
			e._$addClassName(this.yzbtn2,'disabled');
			//e._$addClassName(this.saveBtn2,'disabled');
			this.time2 = 60;
			this.timer2 = window.setInterval(function(){
				this.__onShowLefTime2(this.time2);
			}._$bind(this),1000);
        };
        pro.__onShowLefTime2 = function(i){
        	if(i>=10){
        		this.yzbtn2.innerText = '获取中(' + i + ')';
	    	}else{
	    		this.yzbtn2.innerText = '获取中(0' + i + ')';
	    	}
    		this.time2 = i - 1;
    		if(this.time2 < 0){
    			window.clearInterval(this.timer2);
    			e._$delClassName(this.yzbtn2,'disabled');
    			//e._$delClassName(this.saveBtn2,'disabled');
    			this.yzbtn2.innerText = '获取验证码';
    			this.isSending2 = false;
    		}
    	};
        pro.__onSaveBtnClick2 = function(event){
        	v._$stop(event);
        	
        	var _phonenum2 = this.phonenum2.value.trim(),
	        	_yznum2 = this.yznum2.value.trim();
        	//alert(_phonenum2+', '+_yznum2);
        	
        	//if(this.isLoading2||this.isSending2) return;
        	if(this.isLoading2) return;
        	this.isLoading2 =  true;
        	
        	var _hasError = false,
        		_errorMap = {};
        	if(!_phonenum2){
        		_errorMap['mustfilledin'] = '手机号码不能为空';
        		_hasError = true;
        	//}else if(isNaN(_phonenum2) || _phonenum2.length != 11){
        	}else if(!regExpPhone.test(_phonenum2)){
        		_errorMap['invalidate'] = '请填写正确的手机号码';
        		_hasError = true;
        	}else if(!_yznum2){
        		_errorMap['mustfilledin'] = '验证码不能为空';
        		_hasError = true;
        	}
        	
        	if(_hasError){
        		this.isLoading2 = false;
        		this.__showErrors(_errorMap,this.mInfo2);
        		return;
        	}
        	
        	//this.__showMsgs('保存中...','saving');  // 正在保存...
        	
        	// dwr sending & callback function
        	this.__doPostSaveDWR(this.form2._$data(),this.mInfo2,2);
        	
        	// to to remove 2:
        	//this.__showMsgs('保存成功',this.mInfo2);
        	//window.location.reload();
        	//this.isLoading2 = false; 
        };
        
        pro.__onValidateBtnClick3 = function(event){
        	v._$stop(event);
        	
        	var _phonenum3 = this.phonenum3.value.trim();
        	//alert(_phonenum3);
        	
        	if(this.isSending3) return;
        	this.isSending3 =  true;
        	
        	var _hasError = false,
        		_errorMap = {};
        	if(!_phonenum3){
        		_errorMap['mustfilledin'] = '手机号码不能为空';
        		_hasError = true;
        	//}else if(isNaN(_phonenum3) || _phonenum3.length != 11){
        	}else if(!regExpPhone.test(_phonenum3)){
        		_errorMap['invalidate'] = '请填写正确的手机号码';
        		_hasError = true;
        	}
        	
        	if(_hasError){
        		this.isSending3 = false;
        		this.__showErrors(_errorMap,this.mInfo3);
        		return;
        	}
        	
        	//this.__showMsgs('发送中...','sending');  // 正在发送...
        	
        	// dwr sending & noneed callback function
        	this.__doPostVerifDWR(_phonenum3,3);
        	
			e._$addClassName(this.yzbtn3,'disabled');
			//e._$addClassName(this.saveBtn3,'disabled');
			this.time3 = 60;
			this.timer3 = window.setInterval(function(){
				this.__onShowLefTime3(this.time3);
			}._$bind(this),1000);
        };
        pro.__onShowLefTime3 = function(i){
        	if(i>=10){
        		this.yzbtn3.innerText = '获取中(' + i + ')';
	    	}else{
	    		this.yzbtn3.innerText = '获取中(0' + i + ')';
	    	}
    		this.time3 = i - 1;
    		if(this.time3 < 0){
    			window.clearInterval(this.timer3);
    			e._$delClassName(this.yzbtn3,'disabled');
    			//e._$delClassName(this.saveBtn3,'disabled');
    			this.yzbtn3.innerText = '获取验证码';
    			this.isSending3 = false;
    		}
    	};
        pro.__onSaveBtnClick3 = function(event){
        	v._$stop(event);
        	
        	var _phonenum3 = this.phonenum3.value.trim(),
	        	_yznum3 = this.yznum3.value.trim();
        	//alert(_phonenum3+', '+_yznum3);
        	
        	//if(this.isLoading3||this.isSending3) return;
        	if(this.isLoading3) return;
        	this.isLoading3 =  true;
        	
        	var _hasError = false,
        		_errorMap = {};
        	if(!_phonenum3){
        		_errorMap['mustfilledin'] = '手机号码不能为空';
        		_hasError = true;
        	//}else if(isNaN(_phonenum3) || _phonenum3.length != 11){
        	}else if(!regExpPhone.test(_phonenum3)){
        		_errorMap['invalidate'] = '请填写正确的手机号码';
        		_hasError = true;
        	}else if(!_yznum3){
        		_errorMap['mustfilledin'] = '验证码不能为空';
        		_hasError = true;
        	}
        	
        	if(_hasError){
        		this.isLoading3 = false;
        		this.__showErrors(_errorMap,this.mInfo3);
        		return;
        	}
        	
        	//this.__showMsgs('保存中...','saving');  // 正在保存...
        	
        	// dwr sending & callback function
        	this.__doPostSaveDWR(this.form3._$data(),this.mInfo3,3);
        	
        	// to to remove 3:
        	//this.__showMsgs('保存成功',this.mInfo3);
        	//window.location.reload();
        	//this.isLoading3 = false;
        };
        
        pro.__doPostVerifDWR = function(_phonenum,flag){
            j._$request('/index/phones/getVerifCode', {
            	headers:{"Content-Type":"application/json;charset=UTF-8"},
    			type:'json',
    		    method:'POST',
    		    data:JSON.stringify({phone:_phonenum}),
                onload: function(_data){
                   // 正常回调处理
                   if(_data.code==200){
	              	   // 成功   
                	   switch(flag){
	             			case 1: 
	             			    this.credential1.value = _data.result.credential;
	     	   				    this.expiredTime1.value = _data.result.expiredTime;
	     	   		    	    this.sign1.value = _data.result.sign;
	             				break;
	             			case 2: 
	             				this.credential2.value = _data.result.credential;
	     	   				    this.expiredTime2.value = _data.result.expiredTime;
	     	   		    	    this.sign2.value = _data.result.sign;
	             				break;
	             			case 3: 
	             				this.credential3.value = _data.result.credential;
	     	   				    this.expiredTime3.value = _data.result.expiredTime;
	     	   		    	    this.sign3.value = _data.result.sign;
	             				break;
	             			default: break;
	             	   }
	   		    	   notify.showError('验证码已发送！');
	               }else{                             
	              	   //失败
	            	   notify.showError('验证码发送失败！');
	               }
                }._$bind(this),
                onerror:function(_error){
                   // 异常处理
                   notify.showError('验证码发送失败，请刷新页面重试！');
                }._$bind(this),
                onbeforerequest:function(_data){
                   // 请求发送前，对请求数据处理
                }
            });
        };
        pro.__doPostSaveDWR = function(data,mInfo,flag){
            j._$request('/index/phones/savePhonenum', {
            	headers:{"Content-Type":"application/json;charset=UTF-8"},
    			type:'json',
    		    method:'POST',
    		    data:JSON.stringify(data),
                onload: function(_data){             // 见上面的cbSave函数
                    // 正常回调处理
                  	if(_data.code=='200'){
                  		// 成功               // case1
              			this.__showMsgs('手机号码保存成功！',mInfo);
              			setTimeout(function(){window.location.reload();}, 2000);
                  	} else{                               // case2
                  		//失败
                  		var _errorMap = {};
                  		//_errorMap['schfailed'] = '验证码错误';  // or 号码重复
                  		_errorMap['schfailed'] = _data.result.msg;
                  		this.__showErrors(_errorMap,mInfo);
                  		switch(flag){
                  			case 1: this.isLoading1 = false; break;
                  			case 2: this.isLoading2 = false; break;
                  			case 3: this.isLoading3 = false; break;
                  			default: break;
                  		}
                  	}
                }._$bind(this),
                onerror:function(_error){
                    // 异常处理
                    notify.showError('手机号码绑定失败，请刷新页面重试！');
                    switch(flag){
          				case 1: this.isLoading1 = false; break;
	          			case 2: this.isLoading2 = false; break;
	          			case 3: this.isLoading3 = false; break;
	          			default: break;
	          		}
                }._$bind(this),
                onbeforerequest:function(_data){
                    // 请求发送前，对请求数据处理
                }
            });
        };
        pro.__showErrors = function(_errorMap,_msgWrap){
        	var _html = '';
        	
        	if(!!_errorMap['mustfilledin']){
        		_html += '<p class="w-msg">' + _errorMap['mustfilledin'] + '</p>';
        	}
        	if(!!_errorMap['invalidate']){
        		_html += '<p class="w-msg">' + _errorMap['invalidate'] + '</p>';
        	}
        	if(!!_errorMap['schfailed']){
        		_html += '<p class="w-msg">' + _errorMap['schfailed'] + '</p>';
        	}
        	
        	_msgWrap.innerHTML = _html;
        	if(!!_html){
        		_msgWrap.style.display = '';
        	}else{
        		_msgWrap.style.display = 'none';
        	}
        };
        pro.__showMsgs = function(_msg,_msgWrap){
        	var _html = '';
        	if(!!_msg){
        		_html = '<p class="w-msg w-msg-1">' + _msg + '</p>';
        	}
        	
        	_msgWrap.innerHTML = _html;
        	if(!!_html){
        		_msgWrap.style.display = '';
        	}else{
        		_msgWrap.style.display = 'none';
        	}
        };

        p._$$PhonesModule._$allocate();
    });