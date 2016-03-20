/*
 * ------------------------------------------
 * 新手指南布局模块
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'util/dispatcher/module',
    'pro/page/helpcenter/module'
],function(_k,_v,_e,_x,_m,_p,_o,_f,_r,_pro){
    /**
     * 布局模块
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 构建模块
     * @return {Void}
     */
    _pro.__doBuild = function(){
        this.__super({
            tid:'helpcenter-module-newer'
        });
        
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__export = {
            tab:_list[0],
            parent:_list[1]
        };
    };
    
    // regist module
    _x._$regist('newer',_p._$$Module);
});
