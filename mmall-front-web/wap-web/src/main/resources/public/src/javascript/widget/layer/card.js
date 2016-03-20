/*
 * --------------------------------------------
 * 项目卡片基类实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * --------------------------------------------
 */
define([
    'base/klass',
    'lib/ui/layer/wrapper/card.js'
],
function(_k,_i){
    // variable
    var _pro;
    /**
     * 项目卡片基类
     *
     * @class   {nm.l._$$LCard}
     * @extends {yx.l._$$CardWrapper}
     *
     *
     */
    _p._$$LCard = _k._$klass();
    _pro = _p._$$LCard._$extend(_i._$$CardWrapper);
    /**
     * 控件重置
     * @param {Object} _options
     */
    _pro.__reset = function(_options){
        if(!_options.parent){
            _options.parent = document.body;
        }
        this.__super(_options);
    };

    return _p;
});