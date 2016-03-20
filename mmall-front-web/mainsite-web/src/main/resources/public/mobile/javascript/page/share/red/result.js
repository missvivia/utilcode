/** 
 *使用红包页
 *  主要功能： 使用红包 唤醒App
 *
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
	'{pro}/lib/wap/openApp.js', // 对应对象app(唤醒App相关函数)
	'{pro}/lib/wap/wap.js' // 对应对象_wap(基础工具函数)
], function(app, _wap, p, o, f, r) {
	_wap._$domReady(function() {

		var
			goBtn = _wap._$('.m-goshop'), // 使用红包
			redId = _wap._$getUrlParam('id'); //  获取红包id

		/*唤醒对应红包页的App*/
		app.IphoneOpenUrl = 'mmalliphone://page=8?itemId=' + redId;
		app.AndroidOpenUrl = 'mmallandroid://page=8?itemId=' + redId;
		
		/*使用红包*/
		if(!!goBtn){
			_wap._$addEventListener(goBtn, 'click', function(){
				app.open();
			})
		}
	})
})