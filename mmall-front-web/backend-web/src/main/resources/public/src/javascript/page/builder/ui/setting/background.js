/*
 * ------------------------------------------
 * 背景效果设置控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'base/util',
    'util/form/form',
    'util/template/tpl',
    './setting.js',
    '../colorpick/colorpick.js',
    'text!./background.html'
],function(_k,_v,_e,_u,_t,_l,_i,_x,_html,_p,_o,_f,_r,_pro){
    /**
     * 背景效果设置控件
     */
    _p._$$Background = _k._$klass();
    _pro = _p._$$Background._$extend(_i._$$Setting);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__fopt = {};
        this.__bopt = {
            name:'bgColor',
            defaultColor:'transparent'
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
        this.__bopt.color = _options.bgColor;
        this.__bpicker = _x._$$ColorPicker._$allocate(this.__bopt);
        // reset form data
        this.__form = _t._$$WebForm._$allocate(this.__fopt);
        this.__form._$setValue('opacity',_options.opacity);
    };
    /**
     * 初始化外观
     * @return {Void} 
     */
    _pro.__initXGui = (function(){
        var _seed_html = _l._$addNodeTemplate(_html);
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
        // 0 - web form
        // 1 - background color
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__fopt.form = _list[0];
        this.__bopt.parent = _list[1];
    };
    /**
     * 生成表单数据
     * @return {Void}
     */
    _pro.__doGenSubmitData = function(){
        var _data = this.__form._$data();
        _data.bgColor = this.__bpicker._$getColor()||
                        this.__bopt.defaultColor;
        _data.opacity = parseInt(_data.opacity)||50;
        return _data;
    };
    
    return _p;
});
