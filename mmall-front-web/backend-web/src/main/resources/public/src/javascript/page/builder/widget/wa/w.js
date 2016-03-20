/*
 * ------------------------------------------
 * 固定组件 - 背景设置组件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/element',
    'util/template/tpl',
    '../base/banner.js',
    '../../ui/droper/droper.js',
    '../../ui/colorpick/colorpick.js',
    '../../widget.js',
    '../../util.js',
    'text!./w.html'
],function(_k,_u,_e,_l,_i,_y,_x,_w,_z,_html,_p,_o,_f,_r,_pro){
    /**
     * 固定组件 - 背景设置组件
     * 
     * @class   _$$Widget
     * @extends _$$Banner
     */
    _p._$$Widget = _k._$klass();
    _pro = _p._$$Widget._$extend(_i._$$Banner);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__kopt = {
            defaultColor:'transparent',
            onchange:this.__onColorChange._$bind(this)
        };
        this.__tbop = {
            title:'背景设置',
            onactbuild:this.__doInitColorPick._$bind(this)
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        // init data
        var _setting = _options.setting||_o;
        this.__kopt.color = _setting.bgColor;
        this.__super(_options);
    };
    /**
     * 初始化外观
     * @return {Void}
     */
    _pro.__initXGui = (function(){
        var _seed_html = _u._$uniqueID();
        _l._$addTextTemplate(_seed_html,_html);
        return function(){
            this.__seed_html = _seed_html;
            this.__seed_css = 'm-bd-widget-bn m-bd-widget-a';
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - image box
        var _list = _e._$getByClassName(this.__wrap,'j-flag');
        this.__dopt.parent = _list[0];
    };
    /**
     * 合并容器配置信息
     * @return {Void}
     */
    _pro.__doMergeDropOpt = function(_options){
        this.__dopt.limit = {
            width:1920
        };
        this.__dopt.title = '删除背景图';
        this.__dopt.dataset.boxT1 = _options.bgImgId;
    };
    /**
     * 初始化选色器
     */
    _pro.__doInitColorPick = function(_event){
        var _parent = _event.parent;
        _parent.innerHTML = '<span class="txx">更改背景颜色</span>';
        this.__kopt.parent = _parent;
        this.__picker = _x._$$ColorPicker._$allocate(this.__kopt);
    };
    /**
     * 背景色变化事件
     * @param {Object} _event
     */
    _pro.__onColorChange = function(_event){
        _e._$setStyle(
            this.__parent,
            'backgroundColor',
            _event.color
        );
    };
    /**
     * 资源放入回调
     * @param {Object} _event
     */
    _pro.__doUpdateWidget = function(_event){
        _e._$setStyle(
            this.__parent,
            'backgroundImage',
            'url('+_event.url+')'
        );
    };
    /**
     * 资源删除事件
     * @return {Void}
     */
    _pro.__onResRemove = function(){
        _e._$setStyle(
            this.__parent,
            'backgroundImage','none'
        );
    };
    /**
     * 取布局信息
     * @return {Object} 布局信息
     */
    _pro._$getLayout = function(){
        var _res = this.__droper._$getResource();
        return {
            bgImgId:_res['1']||0,
            bgSetting:JSON.stringify({
                bgColor:_z._$getColor(
                    this.__parent,'backgroundColor'
                )
            })
        };
    };
    
    // regist widget impl
    _w._$regist('a',_p._$$Widget);
    
    return _p;
});
