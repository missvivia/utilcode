/*
 * ------------------------------------------
 * BANNER功能实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'util/event'
],function(_k,_t,_p,_o,_f,_r,_pro){
    /**
     * BANNER功能控件封装
     * 
     * @class   _$$FrmBanner
     * @extends _$$EventTarget
     */
    _p._$$FrmBanner = _k._$klass();
    _pro = _p._$$FrmBanner._$extend(_t._$$EventTarget);
    /**
     * 控件初始化
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__body = _options.parent;
        
        // TODO
    };
    return _p;
});
