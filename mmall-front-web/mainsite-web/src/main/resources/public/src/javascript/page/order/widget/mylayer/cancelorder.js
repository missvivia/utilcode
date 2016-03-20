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
        _cancel_combine = _cancel_html_ui['boxCombine'],
        _oplist = ['买错商品了','订单信息填写错误','红包/优惠券使用问题','支付遇到问题','不想买了','其他原因'];
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
        var _simple = _options.simple||false,
            flag = _options.flag||0,
            tpl = _cancel_simple;
        if(flag == 1){
        	tpl = _cancel_combine;
        }else if(_simple){
        	tpl = _cancel_simple;
        }else{
        	tpl = _cancel_html;
        }
      
        _options.cnt = _t2._$get(tpl,{
           'options':_options.oplist||_oplist
        });
        this.__super(_options);
    };
});