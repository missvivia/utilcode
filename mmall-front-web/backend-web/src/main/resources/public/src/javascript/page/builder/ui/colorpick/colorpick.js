/*
 * ------------------------------------------
 * 颜色选择器控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'ui/base',
    'ui/colorpick/colorpanel',
    'util/template/tpl',
    'util/color/color',
    'text!./colorpick.html'
],function(_k,_v,_e,_i,_n,_l,_x,_html,_p,_o,_f,_r,_pro){
    /**
     * 颜色选择器控件
     */
    _p._$$ColorPicker = _k._$klass();
    _pro = _p._$$ColorPicker._$extend(_i._$$Abstract);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__kopt = {
            clazz:'mpnl',
            onchange:this.__onColorChange._$bind(this)
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // init event
        this.__doInitDomEvent([[
            this.__body,'click',
            this.__onAction._$bind(this)
        ],[
            document,'click',
            this.__doClosePicker._$bind(this)
        ],[
            this.__ninput,'enter',
            this.__onEnter._$bind(this)
        ]]);
        // init color
        this.__ninput.name = _options.name||'color';
        this.__default = _options.defaultColor||'#FFFFFF';
        this.__kopt.defaultColor = this.__default;
        if (this.__default=='transparent'){
            this.__kopt.defaultColor = '#FFFFFF';
        }
        this.__onColorChange(_options.color||this.__default);
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
        // 0 - color preview
        // 1 - color input
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__nprev = _list[0];
        this.__ninput = _list[1];
        this.__kopt.parent = this.__body;
    };
    /**
     * 回车事件
     * @return {Void}
     */
    _pro.__onEnter = function(){
        var _value = '#'+this.__ninput.value.trim();
        if (!_x._$isColor(_value)){
            _value = this.__default;
        }
        this.__onColorChange(_value);
    };
    /**
     * 操作
     */
    _pro.__onAction = function(_event){
        _v._$stop(_event);
        var _element = _v._$getElement(_event),
            _action = _e._$dataset(_element,'action');
        switch(_action){
            case 'clear':
                this.__doClosePicker();
                this.__onColorChange(this.__default);
            break;
            case 'pick':
                this.__kopt.color = '#'+this.__ninput.value;
                this.__picker = _n._$$ColorPanel._$allocate(this.__kopt);
            break;
        }
    };
    /**
     * 颜色变化事件
     * @param {Object} _color
     */
    _pro.__onColorChange = function(_color){
        if (_color=='transparent'){
            this.__ninput.value = '';
        }else{
            this.__ninput.value = _color.substr(1);
        }
        _e._$setStyle(this.__nprev,'backgroundColor',_color);
        this._$dispatchEvent('onchange',{color:_color});
    };
    /**
     * 关闭颜色选择器
     * @return {Void}
     */
    _pro.__doClosePicker = function(){
        if (!!this.__picker){
            this.__picker = 
                this.__picker._$recycle();
        }
    };
    /**
     * 获取颜色置
     * @return {String} 颜色值
     */
    _pro._$getColor = function(){
        var _value = this.__ninput.value.trim();
        return !_value?'':('#'+_value);
    };
    
    return _p;
});
