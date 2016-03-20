/** 
 *无列表显示  绑定唤醒App：
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
	'{pro}/widget/webview/openApp.js', // 对应对象app(唤醒App相关函数)
	'{pro}/widget/webview/wap.js' // 对应对象wap(基础工具函数)
], function(app, _wap, p, o, f, r) {
	_wap._$domReady(function() {
		
		var listId = _wap._$getUrlParam('scheduleId'); // 专场ID
		/*唤醒对应专场页的App*/
		app.IphoneOpenUrl = 'mmall://m.023.baiwandian.cn?page=1&itemId=' + listId; // 唤醒iPhone客户端对应PO页
		app.AndroidOpenUrl = 'mmallandroid://m.023.baiwandian.cn?page=1&itemId=' + listId; // 唤醒Android客户端对应PO页
		
	})
})