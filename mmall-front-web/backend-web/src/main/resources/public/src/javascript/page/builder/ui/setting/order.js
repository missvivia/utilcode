/*
 * ------------------------------------------
 * 三级类目排序设置控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'base/util',
    'util/template/tpl',
    'util/template/jst',
    'util/sort/vertical',
    './setting.js',
    '../../cache/product.js',
    'text!./order.html'
],function(_k,_v,_e,_u,_l,_x,_t,_i,_d,_html,_p,_o,_f,_r,_pro){
    /**
     * 三级类目排序设置控件
     */
    _p._$$Order = _k._$klass();
    _pro = _p._$$Order._$extend(_i._$$Setting);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__sopt = {
            clazz:'j-item',
            selected:'active',
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
        this.__sorter = _t.
            _$$VSortable._$allocate(this.__sopt);
        this.__cache = _d._$$CacheProduct._$allocate({
            oncategoryload:this.__onCategoryLoad._$bind(this)
        });
        this.__cache._$getCategory();
    };
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        this.__super();
        this.__sopt.parent.innerHTML = '&nbsp;';
    };
    /**
     * 初始化外观
     * @return {Void} 
     */
    _pro.__initXGui = (function(){
        var _seed = _l._$parseUITemplate(_html);
        return function(){
            this.__seed_html = _seed.mtpl;
            this.__seed_list = _seed.ltpl;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        this.__sopt.parent = _e._$getByClassName(
            this.__body,'j-flag'
        )[0];
    };
    /**
     * 类目载入完成
     * @return {Void}
     */
    _pro.__onCategoryLoad = function(){
        _x._$render(
            this.__sopt.parent,
            this.__seed_list,{
                xlist:this.__cache._$getSortCategory()
            }
        );
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
