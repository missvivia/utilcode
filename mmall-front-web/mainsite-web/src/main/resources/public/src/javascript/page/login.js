/*
 * ------------------------------------------
 * 登录页
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'pro/widget/module',
    '{pro}widget/ui/login/login.js?20150909',
    'pro/widget/layer/login/login',
    'base/element',
    'pro/extend/config'
],function(_k,_u,_m,_l,win,_e,config,_p,_o,_f,_r){
    /**
     * 页面模块基类
     * 
     * @class   _$$Module
     * @extends _$$Module
     */
	var _pro;
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    
    var _hash = _u._$string2object(window.location.search.replace("?", ""), "&"),
    _redirectURL = _hash.redirectURL  || ("http://023.baiwandian.cn");
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        if(!this.__loginM){
        	this.__loginM = _l._$$LoginModule._$allocate({
	        	parent:_e._$get('loginCon'),
	        	redirectURL:_redirectURL
	        });
        }
        this.__loginM._$show();
    };
    
    // init page
    _p._$$Module._$allocate();
});