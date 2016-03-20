/**
 *唤醒App
 * 使用：
 * ```javascript
 * //唤醒App初始化
 * app._$inWeiXin = function(){
 *     // TODO 微信提示相关遮罩及交互
 * };
 * app._$open({page:2,id:100001});
 *
 * ```
 * 唤醒对应页面：
 * page: 1.专场 2.单品 3.品牌 4.类目 5.订单详情 6.订单列表 7.购物车 8.红包 9.优惠券
 * id:对应
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define(function (_p, _o, _f, _r) {

    // UA
    var __userAgent = navigator.userAgent;

    // IOS
    var __isIOS = !!__userAgent.match(/(iPhone|iPod|iPad)/i);

    // AOS
    var __isAOS = !!__userAgent.match(/Android/i);

    // WinPhone
    var __isWphone = !!__userAgent.match(/Windows Phone/i);

    // PC
    var __isPC = !__isIOS && !__isAOS && !__isWphone;

    // platform
    var __platform = {'IOS': __isIOS, 'AOS': __isAOS, 'WinPhone': __isWphone, 'PC': __isPC};

    var _$$App = {

        // Iphone 唤醒码
        __IOSOpenUrl: 'mmall://',

        // Iphone App 下载地址
        __IOSGetUrl: 'http://m.023.baiwandian.cn/download',

        // Android 唤醒码
        __AOSOpenUrl: 'mmallandroid://',

        // Android APK 下载地址
        __AOSGetUrl: 'http://m.023.baiwandian.cn/download',

        // PC 或  Wphone 跳转地址
        __jumpUrl: 'http://m.023.baiwandian.cn/download',

        // 唤醒App
        __openApp: function (openUrl, callback) {
            var __ifr = document.createElement('iframe');
            __ifr.src = openUrl;
            __ifr.style.display = 'none';
            document.body.appendChild(__ifr);
            setTimeout(function () {
                callback();
                document.body.removeChild(__ifr);
            }, 1000)

        },

        // 微信内部唤醒App 相关操作
        _$inWeiXin: function () {
        	
        	var _cnt = '';
        	var _mask = document.getElementById('wx_mask');
        	var _cntIos = document.getElementById('wx_ios');
        	var _cntAos = document.getElementById('wx_aos');
        	_mask.style.display = 'block';
    	    if (__isIOS) {
    	    	_cnt = _cntIos;
    	    	_cntIos.style.display = 'block';
    	    	_mask.style.background = '#f2f2f2';
            } else if (__isAOS) {
            	_cnt = _cntAos;
            	_cntAos.style.display = 'block';
            	_mask.style.background = '#ededed';
            } else {
            	_mask.style.display = 'none';
            	 document.location = this.__jumpUrl;
            }
    	    
    	    _mask.onclick = function() {
    	    	_cnt.style.display = 'none';
    	    	_mask.style.display = 'none';
    	    }
        },

        // 当前所在环境
        _$platform: (function () {
            for (var _pf in __platform) {
                if (__platform[_pf]) {
                    return _pf;
                }
            }
        })(),

        /**
         *唤醒App对应页面
         * @param  {Object}    arg0 - 属性page 和 id   可空
         * 调用 app._$open({page:2,id:100001}) 或 app._$open();
         * page说明：1.专场 2.单品 3.品牌 4.类目 5.订单详情 6.订单列表 7.购物车 8.红包 9.优惠券
         * id说明: 1.专场ID 2.单品ID 3.品牌ID 以此类推
         */
        _$open: function (_options) {

            var __hash = '', __openUrl, __getUrl;

            // 组装hash
            if (_options) {

                var __page = _options.page || 0, __id = _options.id || 0;

                if (__page && __id) {
                    __hash = 'm.023.baiwandian.cn?page=' + __page + '&itemId=' + __id;
                }
            }

            /*app下载链接和打开链接初始化*/
            if (__isIOS) {
                __openUrl = this.__IOSOpenUrl + __hash;
                __getUrl = this.__IOSGetUrl;
            } else if (__isAOS) {
                __openUrl = this.__AOSOpenUrl + __hash;
                __getUrl = this.__AOSGetUrl;
            } else {
                __openUrl = this.__jumpUrl;
                __getUrl = this.__jumpUrl;
            }

            /*非 PC WinPhone 唤醒App*/
            if (__isIOS || __isAOS) {
                if (__userAgent.match(/MicroMessenger/i)) {

                    this._$inWeiXin();

                } else {

                    // 唤醒App
                    this.__openApp(__openUrl, function () {
                        document.location = __getUrl;
                    });

                }
            } else {

                // PC WinPhone 跳转
                document.location = __getUrl;
            }
        }

    }
    return _$$App;

})