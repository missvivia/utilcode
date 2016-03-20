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
    'base/event',
    'pro/widget/module',
    'base/element',
    'pro/widget/ui/address/address',
    'util/form/form',
    'pro/extend/request'
],function(_k,_u,_v,_m,_e,Address,_t,_request,_p,_o,_f,_r){
    /**
     * 页面模块基类
     * 
     * @class   _$$Module
     * @extends _$$Module
     */
	var _pro,
	_mobileReg = /1[3|5|7|8|][0-9]{9}/,
	_findParent = function(_elm,tagName){
		while(_elm&&_elm.tagName.toLowerCase()!=tagName.toLowerCase()){
			_elm = _elm.parentNode;
		}
		return _elm;
	}
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        if(typeof(useraddress)=='undefined'){
        	useraddress = null;
        } 
        this.addrui = Address._$allocate({parent:"address",address:useraddress})
    };
    // init page
    _p._$$Module._$allocate();
});