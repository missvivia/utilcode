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
    'text!pro/page/return/steps/template/step2.html',
    'util/ajax/xdr',
    'base/util',
    'base/element',
    'util/form/form',
    'pro/page/return/steps/payMethod/payMethod',
    "pro/components/progress/progress",
    'pro/page/return/steps/returnMoney/returnMoney',
    'util/template/tpl',
    'util/template/jst',
    'base/event',
    'util/event'
], function (_k,_e, _w, $, notify, ListComponent, _html, _j, _u, _e, _fo, PayMethod, progress, ReturnMoney, _t, _t2, _v, _u, _p, _o, _f, _r) {
    var _pro;

    _p._$$StepTwo = _k._$klass();
    _pro = _p._$$StepTwo._$extend(_u._$$EventTarget);
    var _seed = _t._$parseUITemplate(_html);


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

    _pro.__initStep = function (_parentId, _data) {
        this.__data = _data;
        var _list = _data.returns.list;
        this.__initReturnGoods(_parentId);
        this.__initReturnAddress(_parentId, _data);
        this.__initList(_parentId, _list);
        this.__initShowReturnTimeNotify(_parentId);
        this.__initReturnMoney(_parentId, _data.returnForm);
        this.__initActions(_parentId);
    };

    _pro.__reset = function (_options) {
        this.__super(_options);
        this.__initExpForm();
    };

    _pro.__initReturnGoods = function (_parent) {
        var _node = _t._$getNodeTemplate("step2-ntp-returngoods");
        $(_parent)[0].appendChild(_node)
    };

    _pro.__initReturnAddress = function (_parent, _data) {
        var _html = _t2._$get('step2-jst-returnaddr', _data);
        var _node = _e._$html2node(_html);
        $(_parent)[0].appendChild(_node);
    };

    _pro.__bindEvents = function () {
        var that = this;
        $(".w-submit", this.__parent)._$on({"click": function () {
            that.__sendRuquest(that.__submitCallback, that, false, "/_returnorder/applyStep2");
        }});
        
        $(".j-cancelreturn",this.__parent)._$on({"click":function(){
        	_j._$request("/_returnorder/cancelreturn", {
                method: 'get',
                query:"ordPkgId="+that.__data.ordPkgId,
                onbeforerequest: function (_data) {
                    progress.start();
                },
                onload: function (_data) {
                	window.location.href="/myorder";
                    progress.end();
                },
                onerror: function (_error) {
                    notify.notify({type: "error", message: _error.message});
                    progress.end(true);
                }
            });
        	
        }});
    };

    _pro.__initList = function (_parentId, list) {
        var that = this;
        this.__list = new ListComponent();
        this.__list.$inject(_parentId);
        this.__list.setData(list);
        this.__components.push(this.__list);
    };

    _pro.__initPayMethod = function (_payType, _parent) {
        this.__payMethod = PayMethod._$allocate({payType: _payType, parentSelector: _parent});
        this.__components.push(this.__payMethod);
    };

    _pro.__initShowReturnTimeNotify = function (_parent) {
        var _html_key = _t._$addNodeTemplate('<div class="m-box"><div class="m-returntime"><div class="hd"><h2>退款时间</h2></div><div class="bd"><p>我们将在收到退货包裹3天内根据您选择的方式进行退款。退款至网易宝金额即时到账；货到付款退至您填写的银行账户，在线支付（网银、支付平台等）原路退款，<br/>3-15个工作日到账。补贴运费将会以优惠券形式发放至您的百万店账户，即时到账。</p></div></div></div>');
        $(_parent)[0].appendChild(_t._$getNodeTemplate(_html_key))
    };

    _pro.__initReturnMoney = function (_parent, _data) {
        this.__returnMoney = new ReturnMoney({data: _data});
        this.__returnMoney.$inject(_parent, "bottom");
    };
    _pro.__initActions = function (_parent) {
        var _html_key = _t._$addNodeTemplate('<div class="m-btnblk"><a href="javascript:void(0)" class="w-btn3 w-btn3-2 w-submit">提 交</a></div>');
        $(_parent)[0].appendChild(_t._$getNodeTemplate(_html_key))
    }

    _pro.__initExpForm = function () {
        var _expForm = $(".expForm")[0];
        this.__expForm = _fo._$$WebForm._$allocate({
            form: _expForm
        });

    };


    _pro.__getSendRequestParams = function () {
        var req = {}, _expData = this.__expForm._$data();
        req.ordPkgId = this.__data.ordPkgId;
        req.retPkgId = this.__data.retPkgId;
        req.returnExpInfoId = this.__data.returnInfo.returnExpInfoId;
        req.expValue = _expData.expValue;
        req.expNO = _expData.expNO;
        return req;
    };
    _pro.__checkValidity = function () {
        return this.__expForm._$checkValidity();
    };

    _pro.__sendRuquest = function (callback, self, _ignoreCheck, _url) {
        if (!_ignoreCheck && !this.__checkValidity())return;
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
                notify.notify({type: "error", message: _error.message});
                progress.end(true);
            }
        });
    };

    _pro.__submitCallback = function (_data) {
        this._$dispatchEvent("onnext", {"nextData": _data});
//        _v._$dispatchEvent(this.__parent,'next',{"nextData":_data});
    };


    return _p._$$StepTwo;
});