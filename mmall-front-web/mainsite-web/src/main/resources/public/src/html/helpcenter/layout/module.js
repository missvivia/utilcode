/*
 * ------------------------------------------
 * 布局模块
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
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
     * 取APP容器节点
     * @return {Void}
     */
    _pro.__doParseParent = function(){
        return document.mbody;
    };
    /**
     * 构建模块
     * @return {Void}
     */
    _pro.__doBuild = function(){
        this.__super({
            tid:'helpcenter-module-layout'
        });
        
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__export = {
    		sidenav:_list[0],
    		search:_list[1],
    		parent:_list[2]
        };
    };
    
    // regist module
    _x._$regist('app',_p._$$Module);
});
