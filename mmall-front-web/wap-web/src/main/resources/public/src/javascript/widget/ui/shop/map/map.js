/*
 * ------------------------------------------
 * 商店地图控件
 * @version  1.0
 * @author   hzzhangweidong(hzzhangweidong@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
        'base/klass',
        'base/element',
        'text!./map.css',
        'pro/widget/ui/shop/shop'
],function(_k,_2,_css2,_i,_p,_o,_f,_r,_pro){
    /**
     * 商店地图控件
     * 
     * @param    {Object} config   - 配置信息
     * @property {Number} brandId  - 品牌名称
     * @property {Array}  citys    - 有品牌店铺的城市列表
     * 
     */
    _p._$$ShopMobileMap = _k._$klass();
    _pro = _p._$$ShopMobileMap._$extend(_i._$$ShopMap);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
    };
    
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        this.__super();
    };
    
    return _p;
});
