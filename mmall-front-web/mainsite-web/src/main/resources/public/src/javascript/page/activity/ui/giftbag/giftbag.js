/*
 * --------------------------------------------------
 * 活动页领取优惠券遮罩实现文件
 * @version  1.0
 * @author   hzyang_fan(hzyang_fan@corp.netease.com)
 * --------------------------------------------------
 */
NEJ.define([
    'base/klass',                                           //  对应对象   _k       (基类)
    'base/element',											//  对应对象   _e       (节点操作相关)
    'util/chain/chainable',                                 //  对应对象   _$       (节点链式调用相关)
    'ui/base'                                               //  对应对象   _ui      (遮罩层ui)
], function (_k, _e, _$, _ui, _p, _o, _f, _r, _pro) {
    /**
     * 页面模块基类
     *
     * @class   _$$MaskModule
     * @extends _$$Abstract
     */
    _p._$$MaskModule = _k._$klass();
    _pro = _p._$$MaskModule._$extend(_ui._$$Abstract);
    /**
     * 控件重置
     * @protected
     * @method {__reset}
     * @param  {Object} 可选配置参数
     * @return {Void}
     */
    _pro.__reset = function (_options) {
        this.__super(_options);
    };
    /**
     * 初使化UI
     */
    _pro.__initXGui = function () {

    };
    /**
     * 显示窗体
     */
    _pro._$show = function () {
        this.__super();
    };
    /**
     * 关闭窗体
     */
    _pro._$hide = function () {
        _e._$removeByEC(_$('.m-mask')[0]);
        _e._$removeByEC(_$('.m-window-exchange')[0]);
    }

    return _p;
});