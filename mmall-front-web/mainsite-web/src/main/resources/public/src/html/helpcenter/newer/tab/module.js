/*
 * ------------------------------------------
 * 首页模块
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'util/dispatcher/module',
    'pro/page/helpcenter/module',
    'util/tab/view',
],function(_k,_v,_e,_x,_m,_t,_p,_o,_f,_r,_pro){
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
            tid:'helpcenter-module-newer-tab'
        });
        
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        
        this.__tbview = _t._$$TabView._$allocate({
            list:_e._$getChildren(this.__body),
            oncheck:this.__doCheckMatchEQ._$bind(this)
        });
    };
    /**
     * 刷新模块
     * @param  {Object} 配置信息
     * @return {Void}
     */
    _pro.__onRefresh = function(_options){
    	this.__super(_options);
        this.__tbview._$match(
            _options.href
        );
    };
    /**
     * 验证选中项
     * @param  {Object} 事件信息
     * @return {Void}
     */
    _pro.__doCheckMatchEQ = function(_event){
        if (_event.target=='/help/newer'){
            _event.target = '/help/newer/login/'
        }
        _event.matched = _event.target.indexOf(_event.source)==0
    };
    
    // regist module
    _x._$regist('newer-tab',_p._$$Module);
});
