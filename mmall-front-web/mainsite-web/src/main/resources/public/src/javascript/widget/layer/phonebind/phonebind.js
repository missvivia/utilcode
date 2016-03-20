/*
 * --------------------------------------------
 * xx窗体控件实现
 * @version  1.0
 * @author   zff(hzzhengff@corp.netease.com)
 * --------------------------------------------
 */
define(['base/klass',
        'base/element',
        'base/event',
        'base/util',
        'util/form/form',
        'util/ajax/xdr',
        'util/encode/json',
        'pro/components/notify/notify',
		'pro/widget/layer/window',
		'text!./phonebind.html',
		'util/template/tpl'],
		function(_k,_e,_v,_u,Form,_j,JSON,notify,_BaseWindow,_html,_e1,_p) {
			var _pro, _sup;
			// ui css text
			var _seed_html= _e1._$addNodeTemplate(_html);
			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$SureWindow}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			_p._$$PhoneWindow = _k._$klass();
			_pro = _p._$$PhoneWindow._$extend(_BaseWindow);
			_sup = _p._$$PhoneWindow._$supro;
			/**
			 * 控件重置
			 * @protected
			 * @method {__reset}
			 * @param  {Object} options 可选配置参数
			 *                           clazz
			 *                           draggable
			 *                           mask
			 */
			_pro.__reset = function(options) {
				options.title = "绑定手机号";
				options.clazz +=' w-win w-win-1 w-win-1-1';
				this.__super(options);
				this.__resetBody(options.data);
			};
			
			/**
			 * 初使化UI
			 */
			_pro.__initXGui = function() {
				this.__seed_html = _seed_html;
			};
			
			/**
			 * 初使化UI
			 */
			_pro.__destory = function() {
				this.__super();
			};
			/**
			 * 初使化节点 
			 */
			_pro.__resetBody = function(data) {
				var _findParent = function(_elm,_className){
					while(_elm&&!_e._$hasClassName(_elm,_className)){
						_elm = _elm.parentNode;
					}
					return _elm;
				};
				this.__credential = _e._$get('credential');
				this.__expiredTime = _e._$get('expiredTime');
		    	this.__sign = _e._$get('sign');
				this.__iptPhone = _e._$get('iptphone');
				this.__yzmBtn = _e._$get('yzmBtn');
		    	this.__iptYzm = _e._$get('iptyzm');
				this.__codeErrmsg = _e._$get('codeErrmsg');
				this.__okBtn = _e._$get('okBtn');
		    	this.__ccBtn = _e._$get('ccBtn');
		    	
		    	// init cont
		    	this.__iptPhone.value = data.phonenum;
		    	this.__txtPhone = data.txtphone;
		    	this.__iptPhone0 = data.iptphone0;
		    	this.__phoneErrmsg = data.phoneErrmsg;
		    	this.__form2 = Form._$$WebForm._$allocate({
		    		form: _e._$get('phoneform'),
		    		oninvalid:function(_event){
						var _target = _event.target;
						var _parent = _findParent(_target,'fitm');
						var _list = _parent.getElementsByTagName('label');
						_e._$addClassName(_list[0],'err');
					},
					onvalid:function(_event){
						var _target = _event.target;
						var _parent = _findParent(_target,'fitm');
						var _list = _parent.getElementsByTagName('label');
						_e._$delClassName(_list[0],'err');
					}
		    	});
		    	
		    	_v._$addEvent(this.__ccBtn,'click',this.__onCCClick._$bind(this));
		    	_v._$addEvent(this.__yzmBtn,'click',this.__onYzmBtnClick._$bind(this));
		    	_v._$addEvent(this.__okBtn,'click',this.__onOKClick._$bind(this));
			};
			
			_pro.__onCCClick = function(_event) {
				_v._$stop(_event);
				this._$hide();
			};
			_pro.__onOKClick = function(_event) {
				_v._$stop(_event);
				//this._$dispatchEvent('onok');
				this.__onOkBtnClick(_event);
				//this._$hide();
			};
			
			_pro.__onYzmBtnClick = function(_event){
		    	_v._$stop(_event);
		    	if(this.__isSending) return;
		    	this.__isSending =  true;
		    	
		    	if(this.__form2._$checkValidity(this.__iptPhone)){
		    		// dwr sending & noneed callback function
		        	this.__doPostVerifDWR(this.__iptPhone.value);
		    		// to be removed 1:
		    		//console.log({phonenum:this.__iptPhone.value});
		        	
					_e._$addClassName(this.__yzmBtn,'w-btn-disb');
					//_e._$addClassName(this.__okBtn,'w-btn-disb');
					this.__time = 60;
					this.__timer = window.setInterval(function(){
						this.__onShowLefTime(this.__time);
					}._$bind(this),1000);
		    	}else{
		    		this.__isSending = false;
		    		return;
		    	}
		    };
		    _pro.__onShowLefTime = function(i){
		    	if(i>=10){
		    		this.__yzmBtn.innerHTML = '<a><span>已发送(' + i + ')</span></a>';
		    	}else{
		    		this.__yzmBtn.innerHTML = '<a><span>已发送(0' + i + ')</span></a>';
		    	}
				this.__time = i - 1;
				if(this.__time < 0){
					window.clearInterval(this.__timer);
					_e._$delClassName(this.__yzmBtn,'w-btn-disb');
					//_e._$delClassName(this.__okBtn,'w-btn-disb');
					this.__yzmBtn.innerHTML = '<a><span>发送验证码</span></a>';
					this.__isSending = false;
				}
			};
			_pro.__doPostVerifDWR = function(_phonenum){
		        _j._$request("/profile/getVerifyCode?phonenum="+_phonenum, {
		        	headers:{"Content-Type":"application/json;charset=UTF-8"},
					type:'json',
				    method:'GET',
		            onload: function(_data){
		               // 正常回调处理
		               if(_data.code==200){
		              	   // 成功              
		          		   //alert('验证码已发送！');
		            	   //notify.notify({type: "success", message: "验证码已发送！"});
		            	   this.__credential.value = _data.result.credential;
		   				   this.__expiredTime.value = _data.result.expiredTime;
		   		    	   this.__sign.value = _data.result.sign;
		               }else{                             
		              		//失败
		              		//alert('验证码发送失败！');
		            	    notify.notify({type: "error", message: _data.message});
		               }
		            }._$bind(this),
		            onerror:function(_error){
		               // 异常处理
		               //alert('验证码发送失败，请刷新页面重试！');
		               notify.notify({type: "error", message: "验证码发送失败，请刷新页面重试！"});
		            }._$bind(this),
		            onbeforerequest:function(_data){
		               // 请求发送前，对请求数据处理
		            }
		        });
		    };
		    _pro.__onOkBtnClick = function(_event){
		    	_v._$stop(_event);
		    	//if(this.__isLoading||this.__isSending) return;
		    	if(this.__isLoading) return;
		    	this.__isLoading =  true;
		    	
		    	if(this.__form2._$checkValidity()){
		    		// dwr sending & callback function
		        	this.__doPostSaveDWR(this.__form2._$data());
		    		// to be removed 2:
//		    		console.log(this.__form2._$data());
//		    		alert('手机号码绑定成功！');
//		    		this.__isLoading = false;
		    	}else{
		    		this.__isLoading = false;
		    		return;
		    	}
		    };
		    _pro.__doPostSaveDWR = function(data){
		        _j._$request("/profile/savePhonenum?"+_u._$object2query(data), {
		        	headers:{"Content-Type":"application/json;charset=UTF-8"},
					type:'json',
				    method:'GET',
		            onload: function(_data){             // 见上面的cbSave函数
		              // 正常回调处理
		              if(_data.code==200){
		              		// 成功              
		          			//alert('手机号码绑定成功！');
		          			
		          			this._$hide();
				    		var _phone = this.__iptPhone.value;
				    		this.__iptPhone0.value = _phone;
				    		
//				    		if(_e._$hasClassName(this.__iptPhone0.nextSibling,'js-error')){
//				    			this.__iptPhone0.nextSibling.style.display='none';
//				    		}
				    		this.__phoneErrmsg.style.display = 'none';
				    		
				    		this.__txtPhone.innerText = _phone.substring(0,3)+'****'+_phone.substring(7);
				    		this.__txtPhone.style.display = '';
				    		this.__codeErrmsg.style.display = 'none';
		              }else{                             
		              		//失败
		              		//alert('验证码错误');
		              		this.__codeErrmsg.style.display = '';
		              }
		              this.__isLoading = false;
		            }._$bind(this),
		            onerror:function(_error){
		               // 异常处理
		               //alert('手机号码绑定失败，请刷新页面重试！');
		               notify.notify({type: "error", message: "手机号码绑定失败，请刷新页面重试！"});
		               this.__codeErrmsg.style.display = 'none';
		               this.__isLoading = false;
		            }._$bind(this),
		            onbeforerequest:function(_data){
		               // 请求发送前，对请求数据处理
		            }
		        });
		    };
		    
			return _p._$$PhoneWindow;
		});