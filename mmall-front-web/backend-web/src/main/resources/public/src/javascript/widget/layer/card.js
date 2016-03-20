/*
 * --------------------------------------------
 * 项目卡片基类实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * --------------------------------------------
 */
define(['{lib}ui/layer/card.wrapper.js'],
function(i,p,o,f,r){
    // variable
    var pro;
    /**
     * 项目卡片基类
     * 
     * @class   {nm.l._$$LCard}
     * @extends {yx.l._$$CardWrapper}
     * 
     * 
     */
    var _$$LCard = NEJ.C();
    pro = _$$LCard._$extend(_i._$$CardWrapper);
    /**
     * 控件重置
     * @param {Object} _options
     */
    pro.__reset = function(_options){
        if(!_options.parent){
            _options.parent = document.body;
        }
        this.__supReset(_options);
    };
    
    return _$$LCard;
});