/*
 * ------------------------------------------
 * 品牌故事验证码
 * @version  1.0
 * @author   hzzhangweidong(hzzhangweidong@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass'
    ,'pro/widget/module'
    ,'util/ajax/xdr'
    ,'util/chain/chainable'
    ,'pro/extend/util'
], function (_k, _m, _j,$,_,_p, _o, _f, _r, _pro) {
    /**
     * 品牌介绍页模块基类
     *
     * @class   _$$Story
     * @extends _$$Story
     */
    _p._$$BusinessInfo = _k._$klass();
    _pro = _p._$$BusinessInfo._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function (_options) {
        this.__super(_options);
    };


    // init page
    _p._$$BusinessInfo._$allocate();
});