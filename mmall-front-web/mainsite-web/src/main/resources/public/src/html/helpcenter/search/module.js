/*
 * ------------------------------------------
 * 搜索模块
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
            tid:'helpcenter-module-search'
        });
        
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__ipt = _list[0];
        this.__searchBtn = _list[1];
        _v._$addEvent(this.__searchBtn,'click',this.__onClickSearchBtn._$bind(this));
        _v._$addEvent(this.__ipt,'keyup',this.__onKeydownSearchBtn._$bind(this));
    };
    
    String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
    
    _pro.__onClickSearchBtn = function(){
    	var _value = this.__ipt.value||'';
    	_value = _value.trim();
    	if(!_value) return;
    	this.__dispatcher._$redirect('/help/resultlist/?keywords='+_value);
    };
    
    _pro.__onKeydownSearchBtn = function(e){
    	_v._$stop(e);
    	if(e.keyCode == 13){
    		this.__onClickSearchBtn();
    	}
    };
    
    // regist module
    _x._$regist('search',_p._$$Module);
});
