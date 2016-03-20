/**
 *唤醒App：
 *更改App的内部链接
 *```javascript
 *openApp.IphoneOpenUrl = 'newsapp://news.163.com/14/1108/18/AAI4JLUL00014JB5.html';
 *openApp.open();
 *```
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
    '{pro}/widget/webview/wap.js'// 对应对象_wap(基础工具函数)
], function (_wap, p, o, f, r) {
    /**************************************************
     *打开App 绑定
     ****************************************************/
    var openApp = {
        // Iphone 唤醒码
        IphoneOpenUrl: 'mmall://',

        // Iphone itunes 唤醒
        IphoneGetUrl: 'http://m.023.baiwandian.cn/download',

        // Android 唤醒码
        AndroidOpenUrl: 'mmallandroid://',

        // Android APK 下载地址
        AndroidGetUrl: 'http://m.023.baiwandian.cn/download',

        // PC 或  Wphone 跳转地址
        jumpUrl: 'http://m.023.baiwandian.cn/download',

        // 唤醒App
        open: function () {
            var
                goUrl = '', // 唤醒码
                getUrl = ''; // app下载地址

            /*app下载链接和打开链接初始化*/
            if (_wap._$isIOS) {
                goUrl = this.IphoneOpenUrl;
                getUrl = this.IphoneGetUrl;
            } else if (_wap._$isAndroid) {
                goUrl = this.AndroidOpenUrl;
                getUrl = this.AndroidGetUrl;
            } else {
                goUrl = this.jumpUrl;
                getUrl = this.jumpUrl;
            }

            /*非 PC Wphone 唤醒App*/
            if (!_wap._$isPC() || !_wap._$isWphone) {
                if (!!_wap._$userAgent.match(/MicroMessenger/i)) {

                	/*微信唤醒App提示页显示*/
                    var mask = _wap._$('.g-downApp-remind'), remind; // 提示页
                    mask.style.display = 'block'; // 显示提示页

                    if (_wap._$isIOS) {
                    	remind = _wap._$('.iosRemind');
                        remind.style.display = 'block';
                        mask.style.background = '#f2f2f2';
                    } else if (_wap._$isAndroid) {
                    	remind = _wap._$('.androidRemind');
                        remind.style.display = 'block';
                        mask.style.background = '#ededed';
                    }else{
                        mask.style.display = 'none';
                    }

                    /*用户点击隐藏提示页*/
                    _wap._$addEventListener(mask, 'click', function () {
                        mask.style.display = 'none';
                        remind.style.display = 'none';
                    })
                } else {

                    /*唤醒App*/
                    _wap._$openApp(goUrl);

                    // 唤醒失败 跳转下载页
                    setTimeout(function () {
                        document.location = getUrl;
                    }, 500)
                }
            } else {

                // PC Wphone 跳转
                document.location = getUrl;
            }
        }
    }

    /*页面头部有打开App便签时 事件绑定*/
    var openBtn = _wap._$('#openMISS');
    if (!!openBtn) {
        /*打开App按钮点击绑定*/
        openBtn.onclick = function () {
            openApp.open()
        }
    }

    // change to NEJ module (唤醒App时有特定内部链接时  修改openApp对应url即可)
    return openApp;
})