/*
 * ------------------------------------------
 * 取消订单实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
/* pro/page/order/widget/mylayer/ordercancel 需要layer.css */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'pro/widget/layer/mockselect/mock.select',
    'util/template/tpl',
    'util/template/jst',
    'text!./ordercancel.html'
],function(_k,_e,_v,_u,_mockselect,_t1,_t2,_html,_p,_o,_f,_r){
    var _pro,
        _html_ui  = _t1._$parseUITemplate(_html),
        _pay_html = _html_ui['boxcomplate'],
        _oplist = [{name:'返还至银行账户(3-15个工作日）',type:'1'},{name:'打入网易宝（即时到帐）',type:'0'}];

    _p._$$OrderCancel = _k._$klass();
    _pro = _p._$$OrderCancel._$extend(_mockselect);

    _pro.__init = function(_options){
        _options.draggable = false;
        _options.mask = true;
        this.__super(_options);
        this._$addEvent('onaction',_options.onaction||_f);
        _v._$addEvent(this.__body,'click',this.__onAction._$bind(this));
    };
    _pro.__onAction = function(_event){
        var _target = _v._$getElement(_event),
            _type   = _e._$dataset(_target,'type');
        if (!!_type || _type === '0'){
            this._$dispatchEvent('onaction',{type:parseInt(_type),reason:''})
        }
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
        _options.tit = '选择退款方式';
        this.__super(_options);
    };
});