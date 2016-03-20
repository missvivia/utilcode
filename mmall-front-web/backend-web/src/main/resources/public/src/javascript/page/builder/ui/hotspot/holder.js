/*
 * ------------------------------------------
 * 热区占位控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'ui/base',
    'util/template/tpl',
    '../droper/droper.js',
    '../../cache/product.js',
    'text!./holder.html'
],function(_k,_v,_e,_i,_l,_x,_d,_html,_p,_o,_f,_r,_pro){
    /**
     * 热区占位控件
     * 
     * @class   _$$HotHolder
     * @extends _$$Abstract
     */
    _p._$$HotHolder = _k._$klass();
    _pro = _p._$$HotHolder._$extend(_i._$$Abstract);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__dopt = {
            title:'删除热区',
            dataset:{boxMatch:2},
            tip:'<span class="ztip">拖入商品</span>',
            ondrop:this.__onResDrop,
            onremove:this.__onResRemove._$bind(this)
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // init dropper
        this.__dopt.dataset.boxT2 = _options.id||'';
        this.__droper = _x._$$Droper._$allocate(this.__dopt);
        // init holder position
        this._$update(_options);
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
        // 0 - size body
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__dopt.parent = _list[0];
    };
    /**
     * 资源方式事件
     * @return {Void}
     */
    _pro.__onResDrop = function(_event){
        _event.stopped = !0;
        var _product = _d._$do(function(_cache){
            return _cache._$getItemInCache(_event.id);
        });
        _e._$addClassName(
            this._$getBody(),
            'j-rmvable'
        );
        var _tip = _product.productName;
        this._$updateTip('<span class="ztip" title="'+_tip+'">'+_tip+'</span>');
    };
    /**
     * 删除资源
     * @return {Void}
     */
    _pro.__onResRemove = function(_event){
        if (!this.__droper) return;
        _e._$delClassName(
            this.__droper._$getBody(),
            'j-rmvable'
        );
        this._$dispatchEvent('onremove',{
            inst:this
        });
        this._$recycle();
    };
    /**
     * 删除热区
     * @return {Void}
     */
    _pro._$remove = function(){
        this.__onResRemove();
    };
    /**
     * 更新占位信息
     * @param {Object} _box
     */
    _pro._$update = function(_box){
        _e._$style(
            this.__body,{
                top:_box.top+'px',
                left:_box.left+'px'
            }
        );
        _e._$style(
            this.__droper._$getBody(),{
                width:_box.width-4+'px',
                height:_box.height-4+'px',
                lineHeight:_box.height-4+'px'
            }
        );
    };
    /**
     * 取热区配置信息
     * @param  {Void}
     */
    _pro._$getSetting = function(){
        var _res = this.__droper._$getResource();
        return {
            top:parseInt(_e._$getStyle(this.__body,'top')),
            left:parseInt(_e._$getStyle(this.__body,'left')),
            height:this.__body.offsetHeight,
            width:this.__body.offsetWidth,
            id:_res['2']||0
        };
    };
    
    return _p;
});
