/** 
 *参看详情页：
 *  主要功能： 加载详情注入
 *
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
    'text!./detailPage.html', // 对应对象tpl(页面加载)
    '{pro}/lib/regularjs/dist/regular.js'// 对应对象window Regular(页面注入)
], function(tpl, p, o, f, r) {
	
	//商品详情参数page
	var detailPage = Regular.extend({
		template : tpl
	});
	
	// change to NEJ module
	return detailPage;
})