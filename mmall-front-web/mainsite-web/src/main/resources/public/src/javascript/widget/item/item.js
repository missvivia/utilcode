/*
 * --------------------------------------------
 * 项目列表项基类实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * --------------------------------------------
 */
define([
    'base/klass',
    'lib/ui/item/list'
],function(_k,_i,_p){
    // variable
    var _pro;
    /**
     * 项目模块基类对象
     *
     * @class   {nm.i._$$Item}
     * @extends {nej.ui._$$ListItem}
     *
     *
     *
     */
    _p._$$Item = _k._$klass();
    _pro = _p._$$Item._$extend(_i._$$ListItem);
    /**
     * 控件节点初始化
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // this.__body
        // TODO other thing
    };

    return _p;
});