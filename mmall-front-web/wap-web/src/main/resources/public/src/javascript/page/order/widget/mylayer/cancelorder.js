/*
 * ------------------------------------------
 * 取消订单实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
/* pro/page/order/widget/mylayer/cancelorder 需要layer.css */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'pro/page/order/widget/mylayer/mylayer',
    'util/template/tpl',
    'util/template/jst',
    'text!./cancelorder.html'
],function(_k,_e,_v,_t0,_t1,_t2,_html,_p,_o,_f,_r){
    var _pro,
        _cancel_html_ui = _t1._$parseUITemplate(_html),
        _cancel_html    = _cancel_html_ui['boxcomplate'],
        _cancel_simple  = _cancel_html_ui['boxsimple'],
        _oplist = ['返还至银行账户(3-15个工作日）','打入网易宝（即时到帐）'];
    _p._$$CancelOrder = _k._$klass();
    _pro = _p._$$CancelOrder._$extend(_t0._$$Mylayer);

    _pro.__init = function(_options){
        _options.clazz = _options.clazz || 'w-win w-win-reason';
        _options.title = _options.title || '您确定要取消订单吗？';
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
        var _simple = _options.simple||false;
        _options.cnt = _t2._$get(_simple ? _cancel_simple : _cancel_html,{
           'options':_options.oplist||_oplist
        });
        this.__super(_options);
    };
});