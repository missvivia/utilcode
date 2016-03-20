/*
 * --------------------------------------------------
 * 活动页领取优惠券遮罩实现文件
 * @version  1.0
 * @author   hzyang_fan(hzyang_fan@corp.netease.com)
 * --------------------------------------------------
 */
NEJ.define([
    '{lib}base/klass.js',                                   //  对应对象   _k       (基类)
    'util/cache/cookie',                                    //  对应对象   _j       (cookie)
    'pro/extend/util',                                      //  对应对象   _u       (基本工具)
    'util/chain/chainable',                                 //  对应对象   _$       (节点链式调用相关)
    'pro/extend/request',                                   //  对应对象   _request (请求相关)
    '{pro}widget/layer/window.js',                          //  对应对象   _window  (全屏遮罩组件)
    'text!./cnt.html',                                 		//  对应对象   _tpl     (插入文本模板)
    'text!./giftbag.html',                                  //  对应对象   _html    (插入文本容器)
    'util/template/tpl',                                    //  对应对象   _t       (页面模板组件)
    'pro/page/activity/ui/giftbag/giftbag',                 //  对应对象   _cnt     (插入文本内容)
    'base/element'                                          //  对应对象   _e       (节点工具)
], function (_k, _j, _u, _$, _request, _window, _tpl, _html, _t, _cnt, _e, _p, _o, _f, _r, _pro) {
    // 注入html
    var __html = _t._$addNodeTemplate(_html);
    // 注入弹窗内容
    var __giftbag = Regular.extend({
        template: _tpl
    });
    /**
     * 页面模块基类
     *
     * @class   _$$Mask3
     * @extends _BaseWindow
     */
    _p._$$Mask = _k._$klass();
    _pro = _p._$$Mask._$extend(_window);
    /**
     * 控件重置
     * @param {Object} _options
     */
    _pro.__reset = function (_options) {

        _options.title = '<img src="/res/images/activity/activity_logo.jpg" />';
        _options.clazz = "m-window-exchange";

        this.__super(_options);

        var __mask = _cnt._$$MaskModule._$allocate({
            parent: document.body
        });

        var __maskCnt;
        var __islogin = _u.isLogin();
        var __showOnce =  !!_j._$cookie('ACTIVITY_REMIND');
        
        if (_options.nolimit || !__showOnce) {
            _request("/activity/getcoupon", {
                method: "post",
                onload: function (_json) {
                    if (!!_json.message && _json.message != '您的帐号已经收到过礼包，无法重复接收。') {
                        __maskCnt = new __giftbag({
                            data: {
                                permission: false,
                                errmsg: _json.message
                            }
                        }).$inject(".j-giftbag");
                        __mask._$show();
                        _$('.err-close')._$on('click', function () {
                            __mask._$hide();
                        })
                    } else {
                        __maskCnt = new __giftbag({
                            data: {
                                permission: true,
                                isnew: !_json.message
                            }
                        }).$inject(".j-giftbag");
                        __mask._$show();
                        _$('.btn-close')._$on('click', function () {
                            __mask._$hide();
                        })
                    }
                },
                onerror: function () {
                	__mask._$hide();
                }
            })
            if(!_options.nolimit)  _j._$cookie('ACTIVITY_REMIND', '1');
        }
    };
    /**
     * 初使化UI
     */
    _pro.__initXGui = function () {
        this.__seed_html = __html;
    };
    /**
     * 初使化节点
     */
    _pro.__initNode = function () {
        this.__super();
        this.__maskCnt = _e._$getByClassName(this.__body, 'j-giftbag')[0];
    };
    /**
     * 显示窗体
     */
    _pro._$show = function () {
        this.__super();
    };

    return _p;
});
