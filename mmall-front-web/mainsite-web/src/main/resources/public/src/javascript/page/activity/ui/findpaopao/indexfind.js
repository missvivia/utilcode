/*
 * --------------------------------------------------
 * 活动页实现文件
 * @version  1.0
 * @author   hzzhangweidong(hzzhangweidong@corp.netease.com)
 * --------------------------------------------------
 */
NEJ.define([
    'base/klass',
    'util/event',
    'pro/widget/module',                                    //  对应对象   _m       (基本模块)
    'pro/extend/util',                                      //  对应对象   _u       (基本工具)
    'util/cache/cookie',                                    //  对应对象   _j       (cookie)
    'pro/extend/request',                                   //  对应对象   _request (请求相关)
    'util/chain/chainable',                                 //  对应对象   _$       (节点链式调用相关)
    'pro/page/activity/layer/bubblewindow/bubblewindow'
], function (_k,_t, _m, _u, _j, _request, _$, BubbleWindow,_p, _o, _f, _r, _pro) {
    /**
     * 页面模块基类
     *
     * @class   _$$IndexFind
     * @extends _$$Module
     */
    _p._$$IndexFind = _k._$klass();
    _pro = _p._$$IndexFind._$extend(_t._$$EventTarget);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function (_options) {
        this.__super(_options);
        this.__createBubbleWindow();
    };
    
    _pro.__createBubbleWindow=function(){
    	var _paopao=window["paopao"]||{id:111,type:0,position:0};
    	if(_paopao){
    		this.__bubbleLayer=BubbleWindow._$allocate({paopao:_paopao,type:"index"})._$show();
    	}
    };
    
    return _p._$$IndexFind;
});