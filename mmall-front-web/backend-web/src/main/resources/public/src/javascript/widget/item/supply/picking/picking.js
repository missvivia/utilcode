/*
 * --------------------------------------------
 * 拣货单列表项
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * --------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'ui/item/item',
    'util/template/tpl',
    'util/template/jst',
    'text!./picking.html'
],function(_k,_e,_v,_i,_t1,_t2,_html,_p,_o,_f,_r){
    var _pro,
        _body_html = _t2._$add(_html);


    /**
     * 拣货单列表项对象
     *
     */
    _p._$$PickingItem = _k._$klass();
    _pro = _p._$$PickingItem._$extend(_i._$$Item);

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
    	var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        // 事件
        _v._$addEvent(_list[0],'click',this.__onUpdateAction._$bind(this));
    };

   /**
     * 导出
     *
     */
    _pro.__onUpdateAction = function(_event){
        this._$dispatchEvent('onupdate', this.__data);
    };


    return _p;
});
