/*
 * --------------------------------------------
 * xx窗体控件实现
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * --------------------------------------------
 */
define(['base/klass',
        'base/element',
        'base/event',
        'pro/widget/layer/window',
        'text!./mock.select.html',
        'text!./mock.select.css',
        'util/template/tpl'],
function(_k,_e,_v, _BaseWindow,_html,_css,_e1,_p) {
    var pro,
        _seed_html= _e1._$addNodeTemplate(_html),
        _seed_css = _e._$pushCSSText(_css);
    /**
     * 所有分类卡片
     *
     * @class   {nm.l._$$MockSelectWindow}
     * @extends {nm.l._$$CardWrapper}
     *
     *
     */
    _p._$$MockSelectWindow = _k._$klass();
    pro = _p._$$MockSelectWindow._$extend(_BaseWindow);
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
        options.clazz +=' w-win';
        this.__cnt.innerHTML = options.cnt||'';
        this.__tit.innerText = options.tit||'修改支付方式';
        this.__supReset(options);
    };

    /**
     * 初使化UI
     */
    pro.__initXGui = function() {
        this.__seed_html = _seed_html;
        this.__seed_css  = _seed_css;
    };

    /**
     * 初使化UI
     */
    pro.__destory = function() {
        this.__super();
    };
    /**
     * 初使化节点
     */
    pro.__initNode = function() {
        this.__super();
        var list = _e._$getByClassName(this.__body, 'j-node');
        this.__tit = list[0];
        this.__cnt = list[1];
        _v._$addEvent(list[2], 'click', this.__onCCClick._$bind(this));
    };

    pro.__onCCClick = function(event) {
        _v._$stop(event);
        this._$hide();
    };

    return _p._$$MockSelectWindow;
})