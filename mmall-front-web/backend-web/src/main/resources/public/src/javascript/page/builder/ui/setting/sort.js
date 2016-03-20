/*
 * ------------------------------------------
 * 商品排序设置控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'base/util',
    'util/list/waterfall',
    'util/template/tpl',
    'util/sort/horizontal',
    './setting.js',
    '../../cache/product.js',
    'text!./sort.html'
],function(_k,_v,_e,_u,_m,_l,_t,_i,_d,_html,_p,_o,_f,_r,_pro){
    /**
     * 商品排序设置控件
     */
    _p._$$Sorter = _k._$klass();
    _pro = _p._$$Sorter._$extend(_i._$$Setting);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__mopt = {
            limit:44,
            delta:100,
            cache:{
                clear:!0,
                lkey:'product-sort',
                klass:_d._$$CacheProduct
            }
        };
        this.__sopt = {
            clazz:'j-item',
            selected:'j-selected',
            placeholder:_e._$create('div','j-holder')
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // init widget
        this.__sorter = _t._$$HSortable._$allocate(this.__sopt);
        this.__lmdl = _m._$$ListModuleWF._$allocate(this.__mopt);
    };
    /**
     * 初始化外观
     * @return {Void} 
     */
    _pro.__initXGui = (function(){
        var _seed = _l._$parseUITemplate(_html);
        return function(){
            this.__seed_html = _seed.mid;
            this.__mopt.item = _seed.jid;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - list box
        // 1 - load more button
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__sopt.parent = _list[0].parentNode;
        this.__mopt.parent = _list[0];
        this.__mopt.more = _list[1];
        this.__mopt.sbody = this.__sopt.parent;
    };
    /**
     * 生成表单数据
     * @return {Void}
     */
    _pro.__doGenSubmitData = function(){
        return {
            value:this.__sorter._$getSortList()
        };
    };
    
    return _p;
});
