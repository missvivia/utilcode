/*
 * ------------------------------------------
 * 自定义组件 - 商品组件(4图)
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'util/template/tpl',
    '../base/product.js',
    '../../widget.js',
    'text!./w.html'
],function(_k,_u,_l,_i,_w,_html,_p,_o,_f,_r,_pro){
    /**
     * 自定义组件 - 商品组件
     * 
     * @class   _$$Widget
     * @extends _$$Product
     */
    _p._$$Widget = _k._$klass();
    _pro = _p._$$Widget._$extend(_i._$$Product);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__popt = {
            limit:{
                width:254,
                height:320
            },
            count:4,
            tip:'商品图<br/><span class="xht">拖拽商品至此处</span>'
        };
        this.__super();
    };
    /**
     * 初始化外观
     * @return {Void}
     */
    _pro.__initXGui = (function(){
        var _seed_html = _u._$uniqueID();
        _l._$addTextTemplate(_seed_html,_html);
        return function(){
            this.__super();
            this.__seed_html = _seed_html;
            this.__seed_css = 'j-widget m-bd-widget-ud m-bd-widget-pd m-bd-widget-f m-bd-widget-6';
        };
    })();
    
    // regist widget impl
    _w._$regist('6',_p._$$Widget);
    
    return _p;
});
