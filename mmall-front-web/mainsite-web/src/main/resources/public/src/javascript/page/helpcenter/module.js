/*
 * ------------------------------------------
 * 页面模块基类
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'util/template/tpl',
    'util/dispatcher/module'
],function(_k,_e,_v,_u,_l,_m,_p,_o,_f,_r,_pro){
    /**
     * 页面模块基类
     * 
     * @class   _$$Module
     * @extends _$$ModuleAbstract
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$ModuleAbstract);
    /**
     * 构建模块
     * @return {Void}
     */
    _pro.__doBuild = function(_options){
        this.__super();
        // build body
        this.__body = _e._$html2node(
            _l._$getTextTemplate(_options.tid)
        );
        if (!this.__body){
            this.__body = _e._$create('div');
        }
        
    };
    
    return _p;
});
