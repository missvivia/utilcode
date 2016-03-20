/*
 * ------------------------------------------
 * 组件列表模块
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'util/dispatcher/module',
    'pro/page/builder/module',
    'pro/page/builder/cache/widget'
],function(_k,_x,_m,_d,_p,_o,_f,_r,_pro){
    /**
     * 组件列表模块
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
        this.__mopt = {
            limit:8,
            item:'builder-module-widget-list',
            cache:{klass:_d._$$CacheWidget}
        };
        this.__super({
            tid:'builder-module-widget'
        });
    };
    // regist module
    _x._$regist('widget',_p._$$Module);
});
