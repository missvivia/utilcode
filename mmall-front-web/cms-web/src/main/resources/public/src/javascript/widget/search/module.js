/*
 * ------------------------------------------
 * 审核列表页面模块
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'pro/widget/module',
    'pro/widget/form'
],function(_k,_$$Module,_t,_p,_o,_f,_r,_pro){
    /**
     * 档期商品审核列表页面
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_$$Module);
    /**
     * 页面初始化
     * @return {Void}
     */
    _pro.__init = function(_options){
        this.__super(_options);
        // init component
        
        // init search form
        var _form = _t._$$WebForm._$allocate({
            form:'search-form',
            onsubmit:function(_data){
            	if(!this.__list){
            		this.__list = new _options.List({
            			data:{
	        				condition:_data
	        				}
            		});
            		this.__list.$inject(_options.parent||'#module-box');
            	}else{
            		this.__list.refresh(_data);
            	}
            }._$bind(this)
        });
    };
    
    return _p;
});
