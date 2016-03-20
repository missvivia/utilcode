/*
 * ------------------------------------------
 * 登录页
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'pro/widget/module',
    'pro/widget/ui/login/login',
    'pro/widget/layer/login/login',
    'base/element',
    'pro/extend/config',
    '../activity0117/explosion/explosion.js'
   
],function(_k,_u,_m,_l,win,_e,config,Explosion,_p,_o,_f,_r){
    /**
     * 页面模块基类
     * 
     * @class   _$$Module
     * @extends _$$Module
     */
	var _pro;
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        var ex = Explosion._$allocate({onanimationend:function(){
        	//ex._$recycle();
        	
        }});
    };
    
    // init page
    _p._$$Module._$allocate();
});