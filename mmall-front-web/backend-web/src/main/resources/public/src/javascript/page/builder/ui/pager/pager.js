/*
 * ------------------------------------------
 * 分页器控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'ui/base',
    'util/template/tpl',
    'util/page/page',
    'text!./pager.html'
],function(_k,_e,_i,_t0,_t1,_html,_p,_o,_f,_r,_pro){
    /**
     * 分页器控件
     */
    _p._$$Pager = _k._$klass();
    _pro = _p._$$Pager._$extend(_i._$$Abstract);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__popt = {
            parented:!0,
            selected:'active',
            disabled:'disabled'
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__popt.index = _options.index;
        this.__popt.total = _options.total;
        this.__popt.onchange = this.
            _$dispatchEvent._$bind(this,'onchange');
        this.__fragment = _t1.
            _$$PageFragment._$allocate(this.__popt);
    };
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        this.__doClearComponent();
        this.__super();
    };
    /**
     * 初始化外观
     * @return {Void} 
     */
    _pro.__initXGui = (function(){
        var _seed_html = _t0._$addNodeTemplate(_html);
        return function(){
            this.__seed_html = _seed_html;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - timer show
        // 1 - edit button
        // 2 - dragger bar
        var _list = _e._$getByClassName(this.__body,'j-flag');
        this.__popt.pbtn = _list.shift();
        this.__popt.nbtn = _list.pop();
        this.__popt.list = _list;
    };
    /**
     * 取当前页码
     *
     * 脚本举例
     * ```javascript
     * // 取当前页码
     * _pager._$getIndex()
     * ```
     *
     * @return {Number} 当前页码
     */
    _pro._$getIndex = function(){
        if (!this.__page) return 1;
        return this.__page._$getIndex();
    };
    /**
     * 取总页数
     *
     * 脚本举例
     * ```javascript
     * // 取总页数
     * _pager._$getTotal()
     * ```
     *
     * @return {Number} 总页数
     */
    _pro._$getTotal = function(){
        if (!this.__page) return 1;
        return this.__page._$getTotal();
    };
    
    return _p;
});
