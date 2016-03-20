/*
 * ------------------------------------------
 * 固定组件 - BANNER组件带倒计时
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'util/template/tpl',
    '../base/banner.js',
    '../../widget.js',
    '../../ui/timer/timer.js'
],function(_k,_u,_l,_i,_w,_ix,_p,_o,_f,_r,_pro){
    /**
     * 固定组件 - BANNER组件带倒计时
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
        this.__topt = {};
        this.__tbop = {
            title:'banner图&倒计时组件'
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__timer = _ix._$$Timer._$allocate(
            _u._$merge({},_options.setting,this.__topt)
        );
    };
    /**
     * 初始化外观
     * @return {Void}
     */
    _pro.__initXGui = function(){
        this.__dataset = {boxInsert:'bottom'};
        this.__seed_css = 'm-bd-widget-bn m-bd-widget-b';
    };
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        this.__topt.parent = this.__wrap;
    };
    /**
     * 合并容器配置信息
     * @return {Void}
     */
    _pro.__doMergeDropOpt = function(_options){
        this.__dopt.limit = {
            width:1090,
            height:[390,600]
        };
        this.__dopt.dataset.boxT1 = _options.headerImgId;
    };
    /**
     * 更新计时器位置
     * @param  {Object} 图片信息
     * @return {Void}
     */
    _pro.__doUpdateWidget = function(_event){
    	if (!!this.__timer){
    		this.__timer._$adjustPostion(
    			_event.height
    		);
    	}
    };
    /**
     * 资源删除事件
     * @return {Void}
     */
    _pro.__onResRemove = function(){
    	if (!!this.__timer){
    		this.__timer._$adjustPostion(
    			this.__dopt.limit.height[0]
    		);
    	}
    };
    /**
     * 取布局信息
     * @return {Object} 布局信息
     */
    _pro._$getLayout = function(){
        var _res = this.__droper._$getResource(),
        	_ret = this.__timer._$getSetting();
        _ret.height = this.__wrap.offsetHeight;
        return {
            headerImgId:_res['1']||0,
            headerSetting:JSON.stringify(_ret)
        };
    };
    // regist widget impl
    _w._$regist('b',_p._$$Widget);
    
    return _p;
});
