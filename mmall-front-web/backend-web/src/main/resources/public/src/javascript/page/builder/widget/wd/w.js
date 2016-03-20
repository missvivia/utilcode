/*
 * ------------------------------------------
 * 固定组件 - 商店地图组件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/element',
    'util/template/tpl',
    'util/toggle/toggle',
    '../base/widget.js',
    '../../widget.js',
    'text!./w.html'
],function(_k,_u,_e,_l,_x,_i,_w,_html,_p,_o,_f,_r,_pro){
    /**
     * 固定组件 - 商店地图组件
     * 
     * @class   _$$Widget
     * @extends _$$Widget
     */
    _p._$$Widget = _k._$klass();
    _pro = _p._$$Widget._$extend(_i._$$Widget);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__tbop = {
            title:'店铺地图组件',
            acts:['view']
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__doUpdateVisible(
            _options.mapPartVisiable
        );
    };
    /**
     * 初始化外观
     * @return {Void}
     */
    _pro.__initXGui = (function(){
        var _seed_html = _u._$uniqueID();
        _l._$addTextTemplate(_seed_html,_html);
        return function(){
            this.__seed_css = 'm-bd-widget-fx m-bd-widget-d';
            this.__seed_html = _seed_html;
        };
    })();
    /**
     * 更新是否可见
     * @return {Void}
     */
    _pro.__doUpdateVisible = function(_visible){
        this.__toolbar._$setVisible(!!_visible);
    };
    /**
     * 取布局信息
     * @return {Object} 布局信息
     */
    _pro._$getLayout = function(){
        return {
            mapPartVisiable:this.__toolbar._$getVisible(),
            mapPartOthers:"{}"
        };
    };
    
    // regist widget impl
    _w._$regist('d',_p._$$Widget);
    
    return _p;
});
