
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
    'base/element',
    'base/event',
    '{pro}extend/util.js',
],function(_k,_m,_e,_v,_,_p,_o,_f,_r,_pro){
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
        //_v._$addEvent(_e._$get('t-down'), 'click', function(){_.smoothTo(_e._$get('a-3-con'),600)});

        // TODO
    };

    // init page
    _p._$$Module._$allocate();
});