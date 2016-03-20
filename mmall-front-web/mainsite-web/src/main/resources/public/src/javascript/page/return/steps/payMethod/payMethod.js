/*
 * ------------------------------------------
 * 我的退货
 * @version  1.0
 * @author   xxx(xxx@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'pro/widget/module',
    'util/chain/chainable',
    'util/ajax/xdr',
    'base/util',
    'util/event',
    "pro/page/return/steps/payMethod/epay/epay",
    "pro/page/return/steps/payMethod/bank/bank"
],function(_k,_w,$,_j,_u,_t,EPay,BankCard,_p,_o,_f,_r){
    var _pro;

    _p._$$PayMethod = _k._$klass();
    _pro = _p._$$PayMethod._$extend(_t._$$EventTarget);


    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(_options){
        this.__super();
        var _payType = _options.payType;
        var _parent= _options.parentSelector;
        this.__determinePayMethod(_payType,_parent);
    };



    _pro.__determinePayMethod=function(_options,_parent){
    	var that =this;
        if(_options === 1){
            //货到付款
            this.__payType= new BankCard();
        }else{
        	//网易宝支付 支付宝
        	this.__payType = new EPay();
        }
        this.__payType.$inject(_parent,"bottom");
        this.__payType.$on("checkSubmit",function(){
        	that._$dispatchEvent("onCheckSubmit");
        });
        this.__payType.$on("calculate",function(){
        	that._$dispatchEvent("onCalculate");
        });
    };

    _pro.getRequestParams=function(){
        return this.__payType.getRequestParams();
    };

    _pro.getRefundType=function(){
        return this.__payType.data.refundType;
    };
    _pro.checkValidity=function(){
        return this.__payType.checkValidity();
    };




    return _p._$$PayMethod;
});