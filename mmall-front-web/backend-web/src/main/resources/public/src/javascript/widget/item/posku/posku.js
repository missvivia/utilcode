/*
 * --------------------------------------------
 * PO列表单项
 * @author   zff(hzzhengff@corp.netease.com)
 * --------------------------------------------
 */
NEJ.define([
    '{lib}base/klass.js',
    '{lib}base/element.js',
    '{lib}base/event.js',
    '{lib}ui/item/item.js',
    '{lib}util/template/tpl.js',
    '{lib}util/template/jst.js',
    'text!./posku.html'
],function(_k,_e,_v,_i,_t1,_t2,_html,_p,_o,_f,_r){
    var _pro,
        _body_html = _t2._$add(_html);


    /**
     * PO列表单项对象
     *
     */
    _p._$$POSKUItem = _k._$klass();
    _pro = _p._$$POSKUItem._$extend(_i._$$Item);

    /**
     * 初始化节点，子类重写具体逻辑
     *
     * @protected
     * @method module:ui/base._$$Abstract#__initNode
     * @return {Void}
     */
    _pro.__initNode = function(){
        if (!this.__seed_html){
            this.__initNodeTemplate();
        }
        this.__body = _t1._$getNodeTemplate(this.__seed_html);
        if (!this.__body){
            this.__body = _e._$create('tr',this.__seed_css);
        }
        _e._$addClassName(this.__body,this.__seed_css);
    };

    /**
     * 刷新列表项
     *
     */
    _pro.__doRefresh = function(_data){
    	_t2._$render(this.__body,_body_html,_data);
    };

    return _p;
});
