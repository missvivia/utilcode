/**
 *下载页微信提示：
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
    '{pro}/widget/webview/wap.js' // 对应对象wap(基础工具函数)
], function(_wap, p, o, f, r) {
    _wap._$domReady(function() {
    	
    	_wap._$('#paopao-header').style.display = 'none';
    	
        _wap._$('.down')._$forEach(function(btn){
            // 点击下载时 检测是否为微信
            btn.onclick = function(){

                if(!!_wap._$userAgent.match(/MicroMessenger/i)){

                    /*微信唤醒App提示页显示*/
                    var mask = _wap._$('.g-downApp-remind'), remind; // 提示页
                    mask.style.display = 'block'; // 显示提示页

                    if (_wap._$isIOS) {
                        remind = _wap._$('.iosRemind');
                        remind.style.display = 'block';
                    } else if (_wap._$isAndroid) {
                        remind = _wap._$('.androidRemind');
                        remind.style.display = 'block';
                    }else{
                        mask.style.display = 'none';
                    }

                    /*用户点击隐藏提示页*/
                    _wap._$addEventListener(mask, 'click', function () {
                        mask.style.display = 'none';
                        remind.style.display = 'none';
                    })
                    return false;
                }
            }
        })
    })
})