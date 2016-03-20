/**
 * 登陆页js
 * @author hzzhangzhoujie@corp.netease.com
 */

define(['util/event',
        'base/event',
        'base/element',
        'util/suggest/suggest',
        'base/util',
        'pro/widget/module',
        'pro/extend/config',
        'pro/extend/util',
        'pro/components/notify/notify'
        ],
    function(_t,_v,_e,_s,_u,_m,config,util,notify,p,o,f,r) {
        var _pro;

        p._$$LoginModule = NEJ.C();
        _pro = p._$$LoginModule._$extend(_m._$$Module);
        
        var _hash = _u._$string2object(window.location.search.replace("?", ""), "&"),
        _redirectURL = _hash.redirectURL  || config.DOMAIN_URL;
        _type = _hash.type  || "normal";
    

     /**
      * 控件重置
      * @protected
      * @method {__reset}
      * @param  {Object} 可选配置参数
      * @return {Void}
      */
        _pro.__reset = function(_options){
    
            this.__supReset(_options);
            // if(_type =="hb"){
            //    this.__resetLayout();
            // }
            this.__form = _e._$get('loginform-p');
            this.__thirdList = _e._$getByClassName(_e._$get('third-list'), 'third');
            this.__input = (_e._$getByClassName(this.__form,'user'))[0];
            this.__psd = (_e._$getByClassName(this.__form,'psd'))[0];
            this.__sugNode = (_e._$getByClassName(this.__form,'sug'))[0];
            this.__commit = (_e._$getByClassName(this.__form,'commit'))[0];
            this.__registerBtn = _e._$get('registerBtn');
            this.__initSuggest();
            _v._$addEvent( this.__commit, 'click', this.__onSubmit._$bind(this));
            _v._$addEvent( this.__registerBtn, 'click', this.__onRegister._$bind(this));
            _v._$addEvent( this.__psd, 'keyup', this.__onKeyUp._$bind(this));
            _v._$addEvent( this.__psd, 'blur', this.__onShowForget._$bind(this,0));
            _v._$addEvent( this.__psd, 'focus', this.__onShowForget._$bind(this,1));
            for(var i=0;i<this.__thirdList.length;i++){
            	_v._$addEvent( this.__thirdList[i], 'click', this.__onRedirectThirdURL._$bind(this,this.__thirdList[i]));
            }
        };
        
    /**
     * 初始化红包登录时的页面
     * @protected
     * @method {__resetLayout}
     * @return {Void}
     */
      _pro.__resetLayout = function(){
          var _parent = _e._$get('m-login-con');
              _list = _e._$getByClassName(_parent, 'j-flag');

          _e._$addClassName(_parent,'m-login-1');
          _e._$delClassName(_list[0],'f-dn');
          _e._$delClassName(_list[1],'f-dn');
          _e._$replaceClassName(_list[2],'form-group-uname','form-group-hb');
          _e._$addClassName(_list[3],'btn-1');

      };    
        
    /**
     * 密码输入框失去（或者获得）焦点
     * @protected
     * @method {__onShowForget}
     * @param  {Object} 事件对象
     * @return {Void}
     */
      _pro.__onShowForget = function(_type){
          var _node = _e._$get('psd-forget');
          if(!_type){
        	  if(_e._$hasClassName(_node,'f-dn'))
        		  _e._$delClassName(_node,'f-dn');
          }else{
        	  _e._$addClassName(_node,'f-dn');
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
            _u._$forEach(_sufix, function(_name,_i){
                if(_index == -1){
                	_arr.push('<li class="unit '+(_i==0?'js-selected':'')+'" data-value="'+ _value + _name +'">'+ _value + _name +'</li>');
                }else if(_name.indexOf(_temp_sufix) != -1){
                    //用户自己输入了@xxx
                    _arr.push('<li class="unit" data-value="'+ _value + _name.substring(_name.indexOf(_temp_sufix)+_temp_sufix.length) +'">'+ _value + _name.substring(_name.indexOf(_temp_sufix)+_temp_sufix.length) +'</li>');
                }
            });
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
            if(!this.__form.protocol.checked){
              return notify.notify("请勾选平台协议!", "warning");
            }
            if(util.trim(this.__form['username'].value) === ''){
                this.__form['username'].focus();
                return false;
            }else if(util.trim(this.__form['password'].value) === ''){
                this.__form['password'].focus();
                return false;
            }
            var preFixUrl=config.DOMAIN_URL+"/authAndRedirect?redirectURL=";
//            this.__form.action = 'https://reg.163.com/logins.jsp?product=paopao&domains=163.com&url='+ encodeURIComponent(preFixUrl+_redirectURL);
            this.__form.action = '';
            this.__form.submit();
        };
        
        /**
         * 注册
         * @return {Void}
         */
        _pro.__onRegister = function(_event){
        	_v._$stop(_event);
//        	window.location.href = 'http://reg.163.com/reg/reg.jsp?product=paopao&from=pagelogin&url='+encodeURIComponent(_redirectURL);
        	window.location.href = '';
        	
        };
        
        /**
         * 第三方登录
         * @return {Void}
         */
        _pro.__onRedirectThirdURL = function(_node,_event){
        	_v._$stop(_event);
        	var _type = _e._$attr(_node,'data-type');
        	window.location.href = '/ext/login/'+_type+'?redirectURL=' + encodeURIComponent(_redirectURL);
        };



        p._$$LoginModule._$allocate();
    });