NEJ.define([
    'base/klass',
    'pro/widget/module',
    './coupon.js'
],function(_k,_w,Coupon,_p,_o,_f,_r){
    var _pro;
    _p._$$Coupon = _k._$klass();
    _pro = _p._$$Coupon._$extend(_w._$$Module);
    _pro.__init = function(){
        this.__super();
    };
    _p._$$Coupon._$allocate({});
    return _p;
});