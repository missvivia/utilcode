/*
 * ------------------------------------------
 * 自定义组件 - BANNER组件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/element',
    'util/template/tpl',
    '../base/banner.js',
    '../../util.js',
    '../../widget.js',
    '../../ui/hotspot/hotspot.js'
],function(_k,_u,_e,_l,_i,_y,_w,_hs,_p,_o,_f,_r,_pro){
    /**
     * 自定义组件 - BANNER组件带热区
     * 
     * @class   _$$Widget
     * @extends _$$Banner
     */
    _p._$$Widget = _k._$klass();
    _pro = _p._$$Widget._$extend(_i._$$Banner);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__hopt = {
            onerror:this.__onError._$bind(this)
        };
        this.__tbop = {
            title:'BANNER组件（请先设置图片）',
            acts:['sort','remove']
        };
        this.__super();
    };
    /**
     * 初始化外观
     * @return {Void}
     */
    _pro.__initXGui = function(){
        this.__super();
        this.__seed_css = 'j-widget m-bd-widget-bn m-bd-widget-ud m-bd-widget-2';
    };
    /**
     * 合并容器配置信息
     * @return {Void}
     */
    _pro.__doMergeDropOpt = function(_options){
        this.__dopt.limit = {
            width:1090,
            height:[10,1500]
        };
        this.__hopt.holders = _options.hotspots;
        this.__dopt.dataset.boxT1 = _options.imgId;
    };
    /**
     * BANNER图片载入成功更新其他组件
     * @return {Void}
     */
    _pro.__doUpdateWidget = function(){
        if (!this.__hotspot){
            this.__hopt.image = this.__droper._$getImage();
            this.__hopt.parent = this.__droper._$getBody();
            this.__hotspot = _hs._$$HotSpot._$allocate(this.__hopt);
        }else{
            this.__hotspot._$update();
            // check overflow hotspot
            var _list = this.__hotspot._$getOverflow();
            if (!!_list){
                _u._$forEach(_list,function(_inst){
                    _inst._$remove();
                });
                this._$dispatchEvent('onmessage',{
                    message:'已自动清除超出BANNER区域的热区'
                });
            }
        }
    };
    /**
     * 资源删除事件
     * @return {Void}
     */
    _pro.__onResRemove = function(){
        if (!!this.__hotspot){
            this.__hotspot._$recycle();
            delete this.__hotspot;
        }
    };
    /**
     * 取布局信息
     * @return {Object} 布局信息
     */
    _pro._$getLayout = function(){
        var _res = this.__droper._$getResource(),
            _ret = {
                hotspots:[],
                imgId:_res['1']||0,
                height:this.__wrap.offsetHeight,
                spaceTop:_y._$getNumber(this.__body,'paddingTop')
            };
        if (!!this.__hotspot){
            _ret.hotspots = this.__hotspot._$getSetting();
        }
        return _ret;
    };
    
    // regist widget impl
    _w._$regist('2',_p._$$Widget);
    
    return _p;
});
