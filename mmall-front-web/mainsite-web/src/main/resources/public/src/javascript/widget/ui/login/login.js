/*
 * --------------------------------------------
 * 登录表单控件实现
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * --------------------------------------------
 */
define(['base/klass',
        'ui/base',
        'base/event',
        'base/element',
        'util/suggest/suggest',
        'util/placeholder/placeholder',
        'base/util',
        '{pro}widget/module.js',
        '{pro}extend/config.js',
        '{pro}extend/util.js',
        'util/template/tpl',
        'util/chain/NodeList',
        'text!./login.css',
	    'text!./login.html?20150909'
        ],
    function(_k,_t,_v,_e,_s,placeholder,_u,Module,config,_,_e1,$,css,html,p,o,f,r) {
        var _pro,
        	_seed_css = _e._$pushCSSText(css),
        	_seed_html= _e1._$addNodeTemplate(html);

        p._$$LoginModule = _k._$klass();
        _pro = p._$$LoginModule._$extend(_t._$$Abstract);
        

     /**
      * 控件重置
      * @protected
      * @method {__reset}
      * @param  {Object} 可选配置参数
      * @return {Void}
      */
        _pro.__reset = function(_options){
            this.__super(_options);
            this.__redirectURL = _options.redirectURL;
        };
        
        _pro.__initXGui = function() {
			//this.__seed_css  = _seed_css;
	        this.__seed_html = _seed_html;
		};
		
		_pro.__initNode = function() {
			this.__super();
			var _list = _e._$getByClassName(this.__body, 'j-flag');
			this.__form = _list[0];
			this.__thirdList = _e._$getByClassName(_list[1], 'item');
            this.__input = (_e._$getByClassName(this.__form,'user'))[0];
            this.__psd = (_e._$getByClassName(this.__form,'psd'))[0];
            this.__redirectError = (_e._$getByClassName(this.__form,'redirectError'))[0];
            this.__redirectURL = (_e._$getByClassName(this.__form,'redirectURL'))[0];
            this.__type = (_e._$getByClassName(this.__form,'type'))[0];
            this.__sugNode = (_e._$getByClassName(this.__form,'sug'))[0];
            this.__commit = (_e._$getByClassName(this.__form,'commit'))[0];
            this.__errorBox = (_e._$getByClassName(this.__form,'errorBox'))[0];
            this.__erricon = (_e._$getByClassName(this.__form,'err_icon'))[0];
            this.__initSuggest();
            placeholder._$placeholder(this.__input,'js-placeholder');
            placeholder._$placeholder(this.__psd,'js-placeholder');
            _v._$addEvent( this.__commit, 'click', this.__onSubmit._$bind(this));
            _v._$addEvent( this.__input, 'keyup', this.__onKeyUp._$bind(this));
            _v._$addEvent( this.__psd, 'keyup', this.__onKeyUp._$bind(this));
            _v._$addEvent(document.body,'keyup', this.__onKeyUp._$bind(this));

            if(document.cookie.indexOf("XYLERR") > -1){
            	var errHtml = decodeURIComponent(this.getCookie("XYLERR"));
            	this.__errorBox.innerHTML = errHtml;
            	_e._$setStyle(this.__erricon, "display", "inline-block");
	            return;
            }

            for(var i=0;i<this.__thirdList.length;i++){
            	_v._$addEvent( this.__thirdList[i], 'click', this.__onRedirectThirdURL._$bind(this,this.__thirdList[i]));
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
//            var _sufix = config.EMAIL_SUFFIX,
//                _arr = [],
//                _index = _value.indexOf("@"),
//                _temp_sufix = _value.substring(_index);
//            _arr.push('<li class="tip">请选择或继续输入...</li>');
//            _u._$forEach(_sufix, function(_name,_i){
//                if(_index == -1){
//                    _arr.push('<li class="unit '+(_i==0?'js-selected':'')+'" data-value="'+ _value + _name +'">'+ _value + _name +'</li>');
//                }else if(_name.indexOf(_temp_sufix) != -1){
//                    //用户自己输入了@xxx
//                    _arr.push('<li class="unit" data-value="'+ _value + _name.substring(_name.indexOf(_temp_sufix)+_temp_sufix.length) +'">'+ _value + _name.substring(_name.indexOf(_temp_sufix)+_temp_sufix.length) +'</li>');
//                }
//            });
//            //this.__sugNode.innerHTML = _arr.join('');
//            //this.__suggest._$setList(_e._$getByClassName(this.__sugNode, 'unit'));
//            this.__suggest._$update(_arr.join(''));
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
            if(_.trim(this.__form['username'].value) === ''){
                this.__form['username'].focus();
                _e._$addClassName(this.__input, "input_error");
                this.__errorBox.innerHTML = "您还没有输入用户名哦";
                _e._$setStyle(this.__erricon, "display", "inline-block");
                return false;
            }else if(_.trim(this.__form['password'].value) === ''){
                this.__form['password'].focus();
                _e._$delClassName(this.__input, "input_error");
                _e._$addClassName(this.__psd, "input_error");
                this.__errorBox.innerHTML = "您还没有输入密码哦";
                _e._$setStyle(this.__erricon, "display", "inline-block");
                return false;
            }else{
            	 _e._$delClassName(this.__psd, "input_error");
            }

            var _list = _e._$getByClassName(this.__body, 'j-flag');
			this.__form = _list[0];
            (_e._$getByClassName(this.__form,'redirectURL'))[0].value = this.__redirectURL;
            
//            var username = this.__form['username'].value,
//            	password = this.__form['password'].value,
//            	redirectURL = this.__redirectURL,
//            	redirectError = this.__redirectError.value;
                
            this.__form.action = 'http://denglu.baiwandian.cn:8100/login';
            this.__form.redirectError.value = "http://023.baiwandian.cn:8095/login";   
            this.__form.submit();
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

        /**
         * 第三方登录
         * @return {Void}
         */
        _pro.__onRedirectThirdURL = function(_node,_event){
        	_v._$stop(_event);
        	var _type = _e._$attr(_node,'data-type');
        	window.location.href = '/ext/login/'+_type+'?redirectURL=' + encodeURIComponent(this.__redirectURL);
        };

        return p;
    });