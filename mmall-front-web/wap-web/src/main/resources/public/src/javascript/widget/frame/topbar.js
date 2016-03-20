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
    'pro/extend/request',
    'pro/widget/app',
    'ui/mask/mask',
    'util/animation/easeinout',
],function(_k,_u,config,_t,_e,_,_v,request,app,_i,_t1,_p,_o,_f,_r,_pro){
    /**
     * 顶栏功能控件封装
     * 
     * @class   _$$FrmTopBar
     * @extends _$$EventTarget
     */
    _p._$$FrmTopBar = _k._$klass();
    _pro = _p._$$FrmTopBar._$extend(_t._$$EventTarget);

    /**
     * 控件初始化
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__menupop = _e._$get('menupop');
        this.__topbar = _e._$get('topbar-box');
        this.__header = _e._$get('paopao-header');
        this.__goTop = _e._$get('gotop'); 
        this.__doInitDomEvent([
//                               ['menu','click',this.__onMenuClick._$bind(this)],
                               [document,'click',this.__onHideMenuCard._$bind(this)],
                               ['closedld','click',this.__onDownloadCloseClick._$bind(this)],
                               ['dldbtn','click',this.__onDownloadBtnClick._$bind(this)],
                               [window,'scroll',this.__onWindowScroll._$bind(this)],
                               [this.__goTop,'click',this.__onGotopClick._$bind(this)]]);
        //jira:1314. 
        //虽然不知道为什么，但是确实解决问题了，IOS safari下 document.click 无法正确相应！！
        // 
        // @remove 找到原因后再移除
        document.body.style.cursor = "pointer";
        if(_.isLogin()){
        	this.__initCart();
        }
        this.__initDownload();
        this.__onWindowScroll();
    };
    _pro.__initDownload = function(){
    	console.log(this.__header.className);
    	try{
    		if(!localStorage.getItem('isDownloadHide')){
    			console.log('isDownloadHide:'+null)
    		} else{
    			console.log(localStorage.getItem('isDownloadHide'));
    		}
    	}catch(e){
    		console.log(e);
    	}
    	if(!localStorage.getItem('isDownloadHide')){
    		console.log('remove hdhide');
    		_e._$delClassName(this.__header,'hdhide');
    	}
    };
//    _pro.__onMenuClick = function(_event){
//    	_v._$stop(_event);
//    	if(_e._$hasClassName(this.__menupop,'show')){
//    		this.__onHideMenuCard();
//    	} else{
//    		_e._$addClassName(this.__menupop,'show');
//    		this.__mask = _i._$$Mask._$allocate({parent:document.body,clazz:'m-mask'});
//    		this.__mask._$show();
//    	}
//    	console.log(_event.target);
//    	location.href = _event.target.parentPage;
//    };
    
    _pro.__onDownloadCloseClick = function(){
    	_e._$addClassName(this.__header,'hdhide');
    	localStorage.setItem('isDownloadHide','1');
    };
    
    _pro.__onHideMenuCard = function(){
    	if(this.__mask){
    		this.__mask = this.__mask._$recycle();
    	}
    	_e._$delClassName(this.__menupop,'show');
    };
    
    _pro.__onDownloadBtnClick = function(){
    	this._$open();
    };
    /**
     *唤醒App对应页面
     * @param  {Object}    arg0 - 属性page 和 id   可空
     * 调用 app._$open({page:2,id:100001}) 或 app._$open();
     * page ：1.专场 2.单品 3.品牌 4.类目 5.订单详情 6.订单列表 7.购物车 8.红包 9.优惠券
     * id说明: 1.专场ID 2.单品ID 3.品牌ID 以此类推
     * _option		
     * 				page		1.专场 2.单品 3.品牌 4.类目 5.订单详情 6.订单列表 7.购物车 8.红包 9.优惠券
     * 				id			1.专场ID 2.单品ID 3.品牌ID 以此类推
     */
    _pro._$setPage = function(_option){
    	this.__appOption = _option;
    };
    
    _pro._$open = function(_option){
    	app._$open(this.__appOption);
    };
    _pro.__initCart = function(){
    	request('/profile/cartandlike',{
    		data:{t:+new Date()},
    		onload:function(_json){
    			if(_json.code==200){
    				if(_json.result.cartCount!=0){
    					_e._$addClassName('topcart','show')
    				}
    			}
    		},
    		onerror:function(){
    			
    		}
    	})
    };
    _pro.__onWindowScroll = function(){
    	if(document.body.scrollTop- window.innerHeight>0){
	    	if(!_e._$hasClassName(this.__goTop,'show')){
	    		_e._$addClassName(this.__goTop,'show')
	    	}
    	} else{
    		_e._$delClassName(this.__goTop,'show');
    	}
    };
    _pro.__onGotopClick = function(){
//    	if(this.__animation){
//    		this.__animation._$recycle();
//    	}
    	var scrollTop = document.body.scrollTop;
    	this.__animation  = _t1._$$AnimEaseInOut._$allocate({
              from:{
                  offset:scrollTop
              },
              to:{
                  offset:0
              },
              duration:500,
              onupdate:function(_event){
            	  document.body.scrollTop = _event.offset;
              },
              onstop:function(){
            	  this.__animation._$recycle();
              }._$bind(this)
          });
          // 开始动画
    	this.__animation._$play();
    };
    
    return _p;
});
