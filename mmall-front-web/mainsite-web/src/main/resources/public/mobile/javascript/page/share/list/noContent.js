/** 
 *无列表显示  绑定唤醒App：
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
	'{pro}/lib/wap/openApp.js', // 对应对象app(唤醒App相关函数)
	'{pro}/lib/wap/wap.js' // 对应对象wap(基础工具函数)
], function(app, _wap, p, o, f, r) {
	_wap._$domReady(function() {
		
		var listId = _wap._$getUrlParam('scheduleId'); // 专场ID
		/*唤醒对应专场页的App*/
		app.IphoneOpenUrl = 'mmalliphone://page=1?itemId=' + listId;
		
	})
})