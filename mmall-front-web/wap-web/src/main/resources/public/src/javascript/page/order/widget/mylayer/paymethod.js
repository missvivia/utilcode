/*
 * ------------------------------------------
 *修改支付方式实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
/* pro/page/order/widget/mylayer/paymethod 需要layer.css */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'pro/widget/layer/mockselect/mock.select',
    'util/template/tpl',
    'util/template/jst',
    'text!./paymethod.html'
],function(_k,_e,_v,_u,_mockselect,_t1,_t2,_html,_p,_o,_f,_r){
    var _pro,
        _html_ui  = _t1._$parseUITemplate(_html),
        _pay_html = _html_ui['boxcomplate'],
        _oplist = [{name:'支付宝',src:'/purchase/repay?pm=2&orderId='},{name:'网易宝',src:'/purchase/repay?pm=0&orderId='},{name:'货到付款',src:'/purchase/toCod?orderId='}];

    _p._$$Paymethod = _k._$klass();
    _pro = _p._$$Paymethod._$extend(_mockselect);

    _pro.__init = function(_options){
        _options.draggable = false;
        _options.mask = true;
        this.__super(_options);
    };
    /**
     * 重置控件
     * @param  {Object} _options - 配置信息
     * @return {Void}
     */
    _pro.__reset = function(_options){
        _u._$forEach(_oplist,function(_op){
            _op.orderId = _options.orderId;
        })
        _options.cnt = _t2._$get(_pay_html,{
           'options':_options.oplist||_oplist
        });
        this.__super(_options);
    };
});