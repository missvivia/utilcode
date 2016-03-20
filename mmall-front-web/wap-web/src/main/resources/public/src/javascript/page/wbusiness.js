/*
 * ------------------------------------------
 * 微信招商页
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'pro/widget/module',
    'pro/widget/webview/swiper/swiper'
],function(_k,_m,Swiper,_p,_o,_f,_r,_pro){
    /**
     * 页面模块基类
     *
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);

        var mySwiper = new Swiper('.swiper-container',{
            mode: 'vertical'
          });
    };

    // init page
    _p._$$Module._$allocate();
});