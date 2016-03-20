/**
 * 登陆页js
 * @author hzzhangzhoujie@corp.netease.com
 */

define(['util/event',
        'base/event',
        'base/element',
//        'util/suggest/suggest',
        'util/placeholder/placeholder',
        'util/form/form',
        'base/util',
        '{pro}widget/module.js',
        '{pro}extend/config.js',
        '{pro}extend/util.js',
        '{pro}extend/request.js',
        'util/encode/md5',
        'util/chain/NodeList',
        ],
    function(_t,_v,_e,placeholder,_f,_u,Module,config,util,Request,_encode,$,p,o,f,r) {    
	var _pro;

        p._$$LoginModule = NEJ.C();
        _pro = p._$$LoginModule._$extend(_t._$$EventTarget);
        
        var _hash = _u._$string2object(window.location.search.replace("?", ""), "&"),
        _redirectURL = _hash.redirectURL  || config.DOMAIN_URL;    

     /**
      * 控件重置
      * @protected
      * @method {__reset}
      * @param  {Object} 可选配置参数
      * @return {Void}
      */
        _pro.__reset = function(_options){
        	//set submit url
            $("#loginform-p")._$attr("action",("http://denglu.baiwandian.cn/login"));
            $("#redirectError")._$val("http://back.baiwandian.cn/login");
            $("#redirectURL")._$val("http://back.baiwandian.cn");
            
            this.__supReset(_options);
            this.__form = _e._$get('loginform-p');
            this.__input = (_e._$getByClassName(this.__form,'user'))[0];
            this.__psd = (_e._$getByClassName(this.__form,'psd'))[0];
            this.__sugNode = (_e._$getByClassName(this.__form,'sug'))[0];
            this.__commit = (_e._$getByClassName(this.__form,'commit'))[0];
//            this.__errorTip = (_e._$getByClassName(this.__form,'error'))[0];
            
//            this.__initSuggest();
            placeholder._$placeholder(this.__input,'js-placeholder');
            placeholder._$placeholder(this.__psd,'js-placeholder');
            _v._$addEvent( this.__commit, 'click', this.__onSubmit._$bind(this));
            _v._$addEvent( this.__psd, 'keyup', this.__onKeyUp._$bind(this));
            _v._$addEvent(document.body,'keyup', this.__onKeyUp._$bind(this));
            //get username and password
            if(document.cookie.indexOf("XYLCMSERR") > -1){
            	$("#errorBox")._$text(decodeURIComponent(this.getCookie("XYLCMSERR")))._$style("display","block");
	            return;
            }
            if(this.getCookie("cms-remember")){
            	document.getElementById("username").value = this.getCookie("cms-username");
            	document.getElementById("password").value = this.getCookie("cms-password");
            	document.getElementById("remember").checked = this.getCookie("cms-remember");
            }
        };
        
      /**
       * 密码输入框键盘
       * @protected
       * @method {__onKeyUp}
       * @param  {Object} 事件对象
       * @return {Void}
       */
        _pro.__onKeyUp = function(_event){
            if(_event.keyCode == 13) {
                this.__commit.click();
            }
        };

        /**
       * 控件重置
       * @protected
       * @method {__reset}
       * @param  {Object} 可选配置参数
       * @return {Void}
       */
        _pro.__initSuggest = function(){
            this.__suggest = _s._$$Suggest._$allocate({
                        body:this.__sugNode,
                        input: this.__input,
                        selected: 'js-selected',
                        onchange: this.__onChange._$bind(this),
                        onselect: this.__onSelect._$bind(this)
                    });
        };

        /**
       * 改变username
       * @protected
       * @method {__onChange}
       * @param  {String} 选中的下拉选项
       * @return {Void}
       */
        _pro.__onChange = function(_value){
            var _sufix = config.EMAIL_SUFFIX,
                _arr = [],
                _index = _value.indexOf("@"),
                _temp_sufix = _value.substring(_index);
            _arr.push('<li class="tip">请选择或继续输入...</li>');
            _u._$forEach(_sufix, function(_name,_i){
                if(_index == -1){
                	_arr.push('<li class="unit '+(_i==0?'js-selected':'')+'" data-value="'+ _value + _name +'">'+ _value + _name +'</li>');
                }else if(_name.indexOf(_temp_sufix) != -1){
                    //用户自己输入了@xxx
                    _arr.push('<li class="unit" data-value="'+ _value + _name.substring(_name.indexOf(_temp_sufix)+_temp_sufix.length) +'">'+ _value + _name.substring(_name.indexOf(_temp_sufix)+_temp_sufix.length) +'</li>');
                }
            });
            //this.__sugNode.innerHTML = _arr.join('');
            //this.__suggest._$setList(_e._$getByClassName(this.__sugNode, 'unit'));
            this.__suggest._$update(_arr.join(''));
      };
      

       /**
       * 选中username
       * @protected
       * @method {__onSelect}
       * @param  {String} 选中的下拉选项
       * @return {Void}
       */
        _pro.__onSelect = function(_value){
            //this.__input.value = _value;
            this.__sugNode.innerHTML = '';
            _e._$style(this.__sugNode,{visibility:'hidden'});
            this.__form['password'].focus();
        };

        /**
       * 登录
       * @protected
       * @method {__onSubmit}
       * @param  {Object} 事件对象
       * @return {Void}
       */
        _pro.__onSubmit = function(_event){
        	_v._$stop(_event);
    		var remember = document.getElementById("remember");
    		var checked = remember.checked;
    		
        	// 验证表单后提交
    		if (_form._$checkValidity()){
    			if(checked){
        			this.setCookie("cms-username",username.value);
        			this.setCookie("cms-password",password.value);
        			this.setCookie("cms-remember",checked);
        		}else{
        			this.delCookie("cms-username",username.value);
        			this.delCookie("cms-password",password.value);
        			this.delCookie("cms-remember",checked);
        		}
    			_form._$submit();
    		}
        }; 
        _pro.setCookie = function(name,value) { 
            var Days = 30; 
            var exp = new Date(); 
            exp.setTime(exp.getTime() + Days*24*60*60*1000); 
            document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString(); 
        }; 
        _pro.getCookie = function(cname){
        	var name = cname + "=";
		    var ca = document.cookie.split(';');
		    for(var i=0; i<ca.length; i++) {
		        var c = ca[i];
		        while (c.charAt(0)==' ') c = c.substring(1);
		        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
		    }
		    return "";
        };
        _pro.delCookie = function(name,value){ 
            var date = new Date(); 
            date.setTime(date.getTime() - 10000); 
            document.cookie = name + "="+ escape (value) +"; expires=" + date.toGMTString(); 
        };
    	var _form = _f._$$WebForm._$allocate({
    		form:'loginform-p',
		    message:{
		        'username':'帐号不能为空',
		        'password':'密码不能为空',
		    }
		});

        p._$$LoginModule._$allocate();
    });