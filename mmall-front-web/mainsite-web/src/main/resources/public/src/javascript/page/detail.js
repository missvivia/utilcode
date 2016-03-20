/*
 * ------------------------------------------
 * 详情页面模块实现文件
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'pro/components/magnifier/magnifier',
    'base/klass',
    'pro/widget/module'
],function(Magifier, _k,_m,_p,_o,_f,_r,_pro){

    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__initMagnifier();
    };

    _pro.__initMagnifier = function(){
        // 初始化放大镜
        this.__magnifier = new Magifier({
            thumb: ".j-thumb",
            ratio: 2
        })
    }
    
    // init page
    _p._$$Module._$allocate();
});