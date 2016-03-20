/*
 * ------------------------------------------
 * 循环播放封装实现文件
 * @author   hzzhangweidong@corp.netease.com
 * ------------------------------------------
 */

NEJ.define([
    'base/klass',
    'base/element',
    'util/template/jst',
    'pro/widget/ui/cycler/cycler',
    'text!./cycler.html'
], function (_k, _e,_j,_c,_html,_p, _o, _f, _r, _pro) {
    var _pro,
        _seed_page2 = _j._$add(_html);

    _p._$$StoryCycle = _k._$klass();
    _pro = _p._$$StoryCycle._$extend(_c._$$Cycler);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function (_options) {
        this.__super(_options);
    };

    _pro.__initXGui = function () {
        this.__super();
        this.__seed_html= _seed_page2;
    };


    return _p;
});