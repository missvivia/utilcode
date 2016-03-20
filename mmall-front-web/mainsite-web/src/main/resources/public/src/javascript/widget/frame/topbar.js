/*
 * ------------------------------------------
 * 顶栏功能实现文件
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'pro/extend/config',
    'util/event',
    'base/element',
    'pro/extend/util',
    'base/event',
    'util/hover/hover',
    'pro/widget/util/datanotify',
    'pro/widget/layer/login/login',
    'pro/extend/request'
],function(_k,_u,config,_t,_e,_,_v,_h,_t1,_l,request,_p,_o,_f,_r,_pro){
    /**
     * 顶栏功能控件封装
     *
     * @class   _$$FrmTopBar
     * @extends _$$EventTarget
     */
    _p._$$FrmTopBar = _k._$klass();
    _pro = _p._$$FrmTopBar._$extend(_t._$$EventTarget);

    var _redirectURL = window.location.href  || config.DOMAIN_URL;
    /**
     * 控件初始化
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        _v._$addEvent(_t1._$$DataNotify, 'barchange',  this.__updatewgt._$bind(this));
        this.__body = _options.parent;
        this.__cartCount = _e._$getByClassName(this.__body, 'j-tb-cart')[0];
      	var _itemList = _e._$getByClassName(this.__body, "item");
        this.__loginNode = _e._$getByClassName(this.__body, "j-flag")[0];
      	this.__nameNode = _e._$getByClassName(this.__body, "j-flag")[1];
        this.__orderNode = _e._$getByClassName(this.__body, "j-flag")[2];
        this.__cartNode = _e._$getByClassName(this.__body, "j-flag")[3];
      	this.__blogins = _e._$getByClassName(this.__body, "login-b");
        this.__flogins = _e._$getByClassName(this.__body, "login-f");
      	this.__userCon = _itemList[0];
      	if(!!this.__userCon){
      		_h._$hover(this.__userCon);
      	}
      	this.__doInitUCenter();
        this.__getData();
        // TODO
        _v._$addEvent( this.__loginNode, 'click', this.__goLoginPage._$bind(this));
        _v._$addEvent( this.__orderNode, 'click', this.__goLoginPage._$bind(this));
        _v._$addEvent( this.__cartNode, 'click', this.__goLoginPage._$bind(this));
    };
    /**
     * 登录
     * @return {Void}
     */
    _pro.__goLoginPage = function(_event){
      _v._$stop(_event);
      var _node = _v._$getElement(_event,'d:href'),
          _href = _e._$dataset(_node,'href')||_redirectURL;
      if(_.isLogin()){
          window.location.href =  _href;
      }else{
          _l._$$LoginWindow._$allocate({
                parent:document.body,
                redirectURL:_href
            })._$show();
      }
      
    };
    /**
     * 初始化用户中心
     * @return {Void}
     */
    _pro.__doInitUCenter = function(){
    	if(_.isLogin()){
    		this._$refresh();
	        for(var i=0;i<this.__flogins.length;i++){
	          _e._$delClassName(this.__flogins[i],"f-dn");
	        }
    	}else{
    		for(var i=0;i<this.__blogins.length;i++){
          _e._$delClassName(this.__blogins[i],"f-dn");
        }
    	}

    };
    /**
     * 刷新用户名
     */
    _pro._$refresh = function(){
    	request('/profile/getUserInfo',{
			type:'json',
			method:'POST',
			onload:function(_json){
				if(_json.code==200){
					this.__nameNode.innerHTML = _json.result.nickname;
					_.setFullUserName(_json.result);
				}
			}._$bind(this)
		})
    };
    /**
     * 获取购物车数量
     * @private
     */
    _pro.__getData = function(){
      _t1._$getBarData();
    }

    /**
     * 更新购物车数量和其他操作 by wuyuedong
     * @private
     */
    _pro.__updatewgt = function(_data){
      var _parent = this.__cartCount.parentNode,
        _cartCount = _data.cartCount;
      if(_cartCount > 0){
        this.__cartCount.innerText = _cartCount;
        _e._$delClassName(_parent, 'f-dn');
      }else{
        this.__cartCount.innerText = 0;
        _e._$addClassName(_parent, 'f-dn');
      }
    }
    return _p;
});
