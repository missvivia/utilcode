/*
 * ------------------------------------------
 * 页面模块实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'pro/widget/module',
    './focus/index.js'
],function(_k,_m,List,_p,_o,_f,_r,_pro){
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
        var list = new List();
        list.$inject('#focus');
        list.$on('loaded',this.__doLazyRefresh._$bind(this));
        // TODO
    };

    // init page
    _p._$$Module._$allocate();
});