/*
 * ------------------------------------------
 * 自定义组件 - 商品组件(2图)
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
                width:537,
                height:370
            },
            count:2,
            match:'1,2'
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
            this.__seed_css = 'j-widget m-bd-widget-ud m-bd-widget-pd m-bd-widget-3';
        };
    })();
    /**
     * 排序占位符
     * @param {Object} _event
     */
    _pro.__onSortHolderUpdate = function(_event){
        if (_event.position=='beforeBegin'){
            _event.left += 3;
        }
    };
    
    // regist widget impl
    _w._$regist('3',_p._$$Widget);
    
    return _p;
});
