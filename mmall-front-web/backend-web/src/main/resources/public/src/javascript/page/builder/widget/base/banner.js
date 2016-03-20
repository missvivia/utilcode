/*
 * ------------------------------------------
 * 自定义组件 - BANNER组件基类
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/element',
    'base/constant',
    'util/template/tpl',
    '../../cache/image.js',
    '../../widget.js',
    '../../ui/droper/droper.js',
    '../../util.js',
    './widget.js'
],function(_k,_u,_e,_g,_l,_d,_cd,_z,_y,_i,_p,_o,_f,_r,_pro){
    /**
     * 自定义组件 - BANNER组件基类
     * 
     * @class   _$$Banner
     * @extends _$$Widget
     */
    _p._$$Banner = _k._$klass();
    _pro = _p._$$Banner._$extend(_i._$$Widget);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__dopt = {
            tip:'拖拽图片至此区域',
            title:'删除BANNER图',
            dataset:{boxMatch:1},
            onerror:this.__onError._$bind(this),
            ondrop:this.__onResDrop._$bind(this),
            onremove:this.__onResRemove._$bind(this),
            onimageok:this.__doUpdateWidget._$bind(this)
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__doMergeDropOpt(_options);
        var _limit = this.__dopt.limit;
        if (!!_limit){
            this.__dopt.tip = '拖拽图片至此区域，宽度'
                            + _y._$limit2str(_limit.width)+'，高度'
                            + _y._$limit2str(_limit.height);
        }
        this.__droper = _z._$$Droper._$allocate(this.__dopt);
    };
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        this.__dopt.parent = this.__wrap;
    };
    /**
     * 合并容器配置信息
     * @return {Void}
     */
    _pro.__doMergeDropOpt = _f;
    /**
     * BANNER图片载入成功更新其他组件
     * @return {Void}
     */
    _pro.__doUpdateWidget = _f;
    /**
     * 资源放入事件
     * @return {Void}
     */
    _pro.__onResDrop = _f;
    /**
     * 资源删除事件
     * @return {Void}
     */
    _pro.__onResRemove = _f;
    
    return _p;
});
