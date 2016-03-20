/*
 * ------------------------------------------
 * 计时器设置控件
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
    'text!./timer.html'
],function(_k,_v,_e,_u,_t,_l,_i,_x,_html,_p,_o,_f,_r,_pro){
    /**
     * 计时器设置控件
     */
    _p._$$Timer = _k._$klass();
    _pro = _p._$$Timer._$extend(_i._$$Setting);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__fopt = {};
        this.__copt = {
            name:'fontColor',
            defaultColor:'#000000'
        };
        this.__bopt = {
            name:'bgColor',
            defaultColor:'transparent'
        };
        this.__zopt = {
            name:'borderColor',
            defaultColor:'#aaaaaa'
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = (function(){
        var _names = [
            'fontFamily','fontWeight','fontSize',
            'opacity','borderStyle','borderWidth'
        ];
        return function(_options){
            this.__super(_options);
            // init widget
            this.__copt.color = _options.fontColor;
            this.__cpicker = _x._$$ColorPicker._$allocate(this.__copt);
            this.__bopt.color = _options.bgColor;
            this.__bpicker = _x._$$ColorPicker._$allocate(this.__bopt);
            this.__zopt.color = _options.borderColor;
            this.__zpicker = _x._$$ColorPicker._$allocate(this.__zopt);
            this.__form = _t._$$WebForm._$allocate(this.__fopt);
            // reset form data
            _u._$forEach(
                _names,function(_name){
                    this.__form._$setValue(
                        _name,_options[_name]
                    );
                },this
            );
        };
    })();
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
        // 1 - font color
        // 2 - background color
        // 3 - border color
        var _list = _e._$getByClassName(this.__body,'j-flag');
        this.__fopt.form = _list[0];
        this.__copt.parent = _list[1];
        this.__bopt.parent = _list[2];
        this.__zopt.parent = _list[3];
    };
    /**
     * 生成表单数据
     * @return {Void}
     */
    _pro.__doGenSubmitData = function(){
        var _data = this.__form._$data();
        _data.bgColor = this.__bpicker._$getColor()||
                        this.__bopt.defaultColor;
        _data.fontColor = this.__cpicker._$getColor()||
                          this.__copt.defaultColor;
        _data.opacity = parseInt(_data.opacity)||50;
        _data.borderColor = this.__zpicker._$getColor()||
                            this.__zopt.defaultColor;
        _data.borderWidth = parseInt(_data.borderWidth)||0;
        return _data;
    };
    
    return _p;
});
