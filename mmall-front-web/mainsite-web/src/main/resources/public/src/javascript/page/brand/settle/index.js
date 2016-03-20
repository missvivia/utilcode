/*
 * ------------------------------------------
 * 品牌入驻页实现文件
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'pro/widget/module',
    'pro/page/brand/settle/list',
    'base/element'
],function(_k,_w,BrandList,_e,_p,_o,_f,_r){
    var _pro;

    _p._$$Coupon = _k._$klass();
    _pro = _p._$$Coupon._$extend(_w._$$Module);

    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(){
    	this.__super();
        this.__brandList = new BrandList();
        this.__brandList.$inject('#g-bd-box');
        
    };

    _p._$$Coupon._$allocate({});

    return _p;
});