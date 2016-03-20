/*
 * --------------------------------------------
 * 项目窗体基类实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * --------------------------------------------
 */
define([
    'base/klass',
    'lib/ui/layer/wrapper/window'
],function(_k, _i, _p){
    var _pro;
    /**
     * 项目窗体基类
     *
     * @class   {nm.l._$$LWindow}
     * @extends {nej.ui._$$WindowWrapper}
     *
     * @param   {}
     */
    _p._$$LWindow = _k._$klass();
    _pro = _p._$$LWindow._$extend(_i._$$WindowWrapper);


    /**
     * 控件重置
     * @param {Object} _options
     */
    _pro.__reset = function(_options){
        _options.parent = _options.parent||document.body;
        if(_options.clazz){
        	_options.clazz = 'm-window '  + _options.clazz;
        } else{
        	_options.clazz = ' m-window';
        }
        if(_options.mask =='none'){
            delete _options.mask
        }else{
            _options.mask = 'm-mask';
        }
        this.__super(_options);
        this.__layer._$setTitle(_options.title,true);
    };

    /**
     * 显示窗体
     */
    _pro._$show = function(){
        this.__super();
        this.__body.focus();
        return this;
    };

    return _p._$$LWindow;
});