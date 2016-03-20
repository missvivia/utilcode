/*
 * ------------------------------------------
 * 页面模块实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'pro/widget/module',
    'pro/components/address/address',
    'pro/widget/ui/address/address',
],function(_k,_e,_m,AddressList,Address,_p,_o,_f,_r,_pro){
    /**
     * 页面模块基类
     *
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__address = Address._$allocate({parent:'address','change':this.__onChange._$bind(this)})
        // TODO
    };
    _pro.__onChange = function(_item){
    	console.log(_item)
    	//this.__address._$recycle();
    	
    }
    // init page
    _p._$$Module._$allocate();
});