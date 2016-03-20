/*
 * --------------------------------------------------
 * 活动页实现文件
 * @version  1.0
 * @author   hzyang_fan(hzyang_fan@corp.netease.com)
 * --------------------------------------------------
 */
NEJ.define([
    'base/klass',                                           //  对应对象   _k       (基类)
    'pro/widget/module',                                    //  对应对象   _m       (基本模块)
    'pro/extend/util',                                      //  对应对象   _u       (基本工具)
    'util/chain/chainable',                                 //  对应对象   _$       (节点链式调用相关)
    'pro/page/activity/components/getcoupon/getcoupon'      //  对应对象   _gc(优惠券兑换状态初始化)
], function (_k, _m, _u, _$, _gc, _p, _o, _f, _r, _pro) {
    /**
     * 页面模块基类
     *
     * @class   _$$Activity
     * @extends _$$Module
     */
    _p._$$Activity = _k._$klass();
    _pro = _p._$$Activity._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function (_options) {
        this.__super(_options);
        this.__initDom();
        this.__initEvent();
    };
    /**
     * 初始化节点
     */
    _pro.__initDom = function () {
        this.__btn = _$('.j-coupon');
    }
    /**
     * 初始化事件
     */
    _pro.__initEvent = function () {
    	// 领取优惠券
    	if(_u.isLogin()) _gc._$getcoupon();
        this.__btn._$on('click',function(){
        	_gc._$getcoupon(true);
        });
    }
    
    // init page
    _p._$$Activity._$allocate();
});