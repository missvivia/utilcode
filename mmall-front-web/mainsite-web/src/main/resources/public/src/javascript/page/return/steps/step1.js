/*
 * ------------------------------------------
 * 我的退货
 * @version  1.0
 * @author   xxx(xxx@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'pro/widget/module',
    'util/chain/chainable',
    'pro/components/notify/notify',
    'pro/page/return/steps/returnList/list',
    'text!pro/page/return/steps/template/step1.html',
    'text!pro/page/return/steps/returnList/list.html',
    'util/ajax/xdr',
    'base/util',
    'pro/page/return/steps/payMethod/payMethod',
    "pro/components/progress/progress",
    'pro/page/return/steps/returnMoney/returnMoney',
    'util/template/tpl',
    'base/event',
    'util/event'
], function (_k,_e, _w, $, notify, ListComponent, _html, _lhtml, _j, _u, PayMethod,progress, ReturnMoney, _t, _v, _u, _p, _o, _f, _r) {
    var _pro,
        _seed_html = _t._$addNodeTemplate(_html);

    _p._$$StepOne = _k._$klass();
    _pro = _p._$$StepOne._$extend(_u._$$EventTarget);


    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function (_options) {
        var _parentId = "#" + _options.parent;
        this.__parent = _e._$get(_options.parent);
        this.__components = [];
        this.__super();
        this.__initStep(_parentId, _options.data);
        this.__bindEvents();

    };

    _pro.__bindEvents = function () {
        var that = this;
        $(".w-submit", this.__parent)._$on({"click": function () {
            that.__sendRuquest(that.__submitCallback, that, false, "/_returnorder/applyReturn");
        }});
    };

    _pro.__initList = function (_parentId, list) {
        var that = this;
        this.__list = new ListComponent({template: _lhtml});
        this.__list.$inject(_parentId);
        this.__list.setData(list);
        this.__components.push(this.__list);
        this.__list.$on("sendQequest",this.__calculateReturnMoney._$bind(this))
    };
    _pro.__calculateReturnMoney=function(){
    	this.__sendRuquest(this.__returnMoney.setData, this.__returnMoney, true, "/_returnorder/add");
    };

    _pro.__initPayMethod = function (_payType, _parent) {
        this.__payMethod = PayMethod._$allocate({payType: _payType, parentSelector: _parent,onCalculate:this.__calculateReturnMoney._$bind(this),onCheckSubmit:this.__preRequest._$bind(this,true)});
        this.__components.push(this.__payMethod);
    };

    _pro.__initShowReturnTimeNotify = function (_parent) {
        $(_parent)[0].appendChild(_t._$getNodeTemplate(_seed_html));
    };

    _pro.__initReturnMoney = function (_parent) {
        var _data = {
            returnPrice: {
                payedTotalPriceToUser:0//退款金额
            },
            returnWay: [//退回的类型
            ]
        };
        this.__returnMoney = new ReturnMoney({data: _data});
        this.__returnMoney.$inject(_parent, "bottom");
    };
    _pro.__initActions = function (_parent) {
        var _html_key = _t._$addNodeTemplate('<div class="m-btnblk"><a href="javascript:void(0)" class="w-btn3 w-btn3-2 w-submit" id="w-submit">提 交 <div class="js-error f-dn js-empty-tip">请选择退货商品和检查退款信息</div></div>');
        $(_parent)[0].appendChild(_t._$getNodeTemplate(_html_key))
    };

    _pro.__initStep = function (_parentId, _data) {
        if (!_data)return;
        this.__data = _data;
        var _list = _data.returns.list;
        var _payType = _data.payMethod.intValue;
        this.__initList(_parentId, _list);
        var _parentSelector = _parentId + " .m-productlist" + " .bd";
        this.__initPayMethod(_payType, _parentSelector);
        this.__initShowReturnTimeNotify(_parentId);
        this.__initReturnMoney(_parentId);
        this.__initActions(_parentId);
    };


    _pro.__getSendRequestParams = function () {
        var req = {};
        req.ordPkgId = this.__data.ordPkgId;
        req.list = this.__list.getRequestParam();
        req.refundType = this.__payMethod.getRefundType();
        req.bankCard = this.__payMethod.getRequestParams() || {};
        return req;
    };
    _pro.__checkValidity = function () {
        for (var i = 0, len = this.__components.length; i < len; i++) {
            if (this.__components[i].checkValidity() == false)
                return false;
        }
        return true;
    };
    
    _pro.__preRequest=function(_ignoreCheck){
    	var _target="w-submit",_disClazz="w-btn4-dis";
        if (this.__checkValidity()){
        	 _e._$delClassName(_target,_disClazz);
        }else{
        	_e._$hasClassName(_target,_disClazz)?"":_e._$addClassName(_target,_disClazz);;
        }
        if(_ignoreCheck ==false&&_e._$hasClassName(_target,_disClazz)){
        	return true;
        }
    };

    _pro.__sendRuquest = function (callback, self, _ignoreCheck, _url) {
    	if(this.__preRequest(_ignoreCheck)){
    		return;
    	}
        var _obj = this.__getSendRequestParams();
        var that = this;
        _j._$request(_url, {
            headers: {"Content-Type": "application/json;charset=UTF-8"},
            method: 'put',
            type: "json",
            data: JSON.stringify(_obj),
            onbeforerequest: function (_data) {
                progress.start();
            },
            onload: function (_data) {
                if (_data) {
                    callback.call(self, _data);
                }
                progress.end();
            },
            onerror: function (_error) {
                progress.end(true);
                notify.notify({type: "error", message: _error.message});
            }
        });
    };
    _pro.__submitCallback = function (_data) {
        this._$dispatchEvent("onnext", {"nextData": _data});
//        _v._$dispatchEvent(this.__parent,'next',{"nextData":_data});
    };


    return _p._$$StepOne;
});