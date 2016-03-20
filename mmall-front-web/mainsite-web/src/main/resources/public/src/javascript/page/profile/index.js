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
    'base/element',
    'util/tab/tab',
    './index/list.js'
],function(_k,_ut,_m,_e,_t,List,_p,_o,_f,_r){
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
       var list = new List();
       list.$inject('#index');
    };
    
    // init page
    _p._$$Module._$allocate();
});