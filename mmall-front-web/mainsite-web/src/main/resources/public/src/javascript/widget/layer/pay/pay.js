/*
 * --------------------------------------------
 * 支付方式窗体控件实现
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * --------------------------------------------
 */
define(['base/klass',
        'base/element',
        'base/event',
        'pro/widget/layer/window',
        'text!./pay.html',
        'util/template/tpl'],
function(_k,_e,_v, _BaseWindow,_html,_e1,_p) {
    var pro, sup;
    // ui css text
    var _seed_html= _e1._$addNodeTemplate(_html);
    /**
     * 所有分类卡片
     *
     * @class   {nm.l._$$PayWindow}
     * @extends {nm.l._$$CardWrapper}
     *
     *
     */
    _p._$$PayWindow = _k._$klass();
    pro = _p._$$PayWindow._$extend(_BaseWindow);
    /**
     * 控件重置
     * @protected
     * @method {__reset}
     * @param  {Object} options 可选配置参数
     *                           clazz
     *                           draggable
     *                           mask
     */
    pro.__reset = function(options) {
        options.clazz +=' w-win w-win-1 w-win-1-3 w-win-1-4';
        this.__super(options);
        this.__orderId = options.orderId||'';
        this.__lk0.href = '/purchase/repay?orderId=' + this.__orderId;
        this.__lk1.href = '/purchase/toCod?orderId=' + this.__orderId;
    };

    /**
     * 初使化UI
     */
    pro.__initXGui = function() {
        this.__seed_html = _seed_html;
    };

    /**
     * 初使化节点
     */
    pro.__initNode = function() {
        this.__super();
        var list = _e._$getByClassName(this.__body, 'j-flag');
        this.__lk0 = list[0];
        this.__lk1 = list[1];
        _v._$addEvent(list[0], 'click', this.__onOKClick._$bind(this));
        _v._$addEvent(list[1], 'click', this.__onCCClick._$bind(this));

    };

    pro.__onCCClick = function(event) {
        this._$hide();
    };
    pro.__onOKClick = function(event) {
        this._$hide();
    }
    return _p._$$PayWindow;
})