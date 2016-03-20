/*
 * --------------------------------------------
 * xx窗体控件实现
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * --------------------------------------------
 */
/*  */
define(['base/klass',
        'base/element',
        'base/event',
        'pro/widget/layer/window',
        'text!./noreturn.html',
        'text!./noreturn.css',
        'util/template/tpl'],
function(_k,_e,_v, _BaseWindow,_html,_css,_e1,_p) {
    var pro,
        _seed_html= _e1._$addNodeTemplate(_html),
        _seed_css = _e._$pushCSSText(_css);
    /**
     * 所有分类卡片
     *
     * @class   {nm.l._$$NoReturnWindow}
     * @extends {nm.l._$$CardWrapper}
     *
     *
     */
    _p._$$NoReturnWindow = _k._$klass();
    pro = _p._$$NoReturnWindow._$extend(_BaseWindow);
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
        options.clazz = (options.clazz ? options.clazz : 'w-noreturn')+' no-close w-win';
        this.__supReset(options);
        this.__cnt.innerHTML = options.cnt||'';
        this.__ok.innerHTML = options.oktxt||'确 定';
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
        this.__cnt = list[0];
        this.__ok  = list[1];
        _v._$addEvent(this.__ok, 'click', this.__onCCClick._$bind(this));
        _v._$addEvent(this.__cnt,'click',this.__onOKClick._$bind(this));
    };

    pro.__onOKClick = function(event){
        this._$dispatchEvent('onok',event);
    };

    pro.__onCCClick = function(event) {
        _v._$stop(event);
        this._$hide();
    };

    return _p._$$NoReturnWindow;
})