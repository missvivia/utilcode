/*
 * ------------------------------------------
 * 我的优惠券实现文件
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'pro/widget/module',
    'pro/page/mycoupon/couponList',
    'pro/page/mycoupon/redpacketList',
    'pro/page/mycoupon/widget/tab/tab',
    'base/element'
],function(_k,_w,CouponList,RedpacketList,tab,_e,_p,_o,_f,_r){
    var _pro;

    _p._$$Coupon = _k._$klass();
    _pro = _p._$$Coupon._$extend(_w._$$Module);

    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(){
    	this.__super();
        this.__tab = new tab();
        this.__tab.$inject('#tab-box');
        this.__tab.$on('change',this.__onTabChange._$bind(this));
        this.__tab.go(0);
        
    };


    /**
     * 切换tab
     * @param  {[type]} _index [description]
     * @param  {[type]} _last  [description]
     * @return {[type]}        [description]
     */
    _pro.__onTabChange = function(_index){
    	if(_index == 0 && !this.__couponList){
    		this.__couponList = new CouponList();
            this.__couponList.$inject('#m-coupon');
    	}else if(_index == 1 && !this.__redpacketList){
    		this.__redpacketList = new RedpacketList();
            this.__redpacketList.$inject('#m-redpacket');
    	}
    	if(_index == 0){
    		if(_e._$hasClassName(_e._$get("m-coupon"),"f-dn"))
    			_e._$delClassName(_e._$get("m-coupon"),"f-dn");
    		_e._$addClassName(_e._$get("m-redpacket"),"f-dn");
    	}else if(_index == 1){
    		_e._$addClassName(_e._$get("m-coupon"),"f-dn");
    		if(_e._$hasClassName(_e._$get("m-redpacket"),"f-dn"))
    			_e._$delClassName(_e._$get("m-redpacket"),"f-dn");
    	}
    	
    };

    _p._$$Coupon._$allocate({});

    return _p;
});