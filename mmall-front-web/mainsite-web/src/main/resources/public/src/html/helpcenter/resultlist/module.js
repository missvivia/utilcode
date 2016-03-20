/*
 * ------------------------------------------
 * 搜索结果模块
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
    'pro/page/helpcenter/widget/resultList'
],function(_k,_v,_e,_x,_m,ResultList,_p,_o,_f,_r,_pro){
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
            tid:'helpcenter-module-resultlist'
        });
        
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
    };
    /**
     * 显示模块
     * @return {Void}
     */
    _pro.__onShow = function(_options){
        this.__super(_options);
        var _srh = _options.param.keywords||"";
        if(!this.__resultList){
        	this.__resultList = new ResultList({
        		data:{condition:{keywords:_srh}}
        	}).$inject('#help-resultlist');
        }
    };
    /**
     * 刷新模块
     * @return {Void}
     */
    _pro.__onRefresh = function(_options){
        this.__super(_options);
        var _srh = _options.param.keywords||"";
        if(!!this.__resultList){
        	this.__resultList.refresh({keywords:_srh});
        }
    };
    
    // regist module
    _x._$regist('resultlist',_p._$$Module);
});
