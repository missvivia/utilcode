/*
 * --------------------------------------------------
 * 活动页领取优惠券遮罩实现文件
 * @version  1.0
 * @author   hzzhangweidong(hzzhangweidong@corp.netease.com)
 * --------------------------------------------------
 */
NEJ.define([
    'base/klass',                                           //  对应对象   _k       (基类)
    'ui/base',                                              //  对应对象   _ui      (遮罩层ui)
    'base/element',
    "pro/page/activity/components/activitybar/activitybar"
], function (_k, _ui, _e,ActivityBar,_p, _o, _f, _r, _pro) {

    /**
     * 页面模块基类
     *
     * @class   _$$MaskModule3
     * @extends _$$Abstract
     */
    _p._$$LotteryModule = _k._$klass();
    _pro = _p._$$LotteryModule._$extend(_ui._$$Abstract);
    /**
     * 控件重置
     * @protected
     * @method {__reset}
     * @param  {Object} 可选配置参数
     * @return {Void}
     */
    _pro.__reset = function (_options) {
        this.__super(_options);
        this.__activitybar=new ActivityBar();
    	this.__activitybar.$inject(_options.parent);
    };

    return _p;
});