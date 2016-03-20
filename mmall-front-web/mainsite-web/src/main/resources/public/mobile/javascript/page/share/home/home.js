/**
 * 说明： 由于html5的 iframe只保留src属性 大部分浏览器也支持别的属性 但是 safari会放大iframe至原网页大小 本页解决 方案：
 * iframe 外的div 增加css属性-webkit-overflow-scrolling:touch;overflow:scroll;
 * 并使用js扩充iframe大小 PS: 在PC下的双层滚动条 是兼容移动端所必须的
 *
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
	'{pro}/lib/wap/openApp.js', // 对应对象app(唤醒App相关函数)
	'{pro}/lib/wap/wap.js' // 对应对象_wap(基础工具函数)
], function(app, _wap, p, o, f, r) {

	var homeManager ={

		// 页面渲染
		_$draw : function() {

			/**************************************************
			 *页面显示
			 ****************************************************/

			this.viewH = _wap._$getWindowHeight(); // 屏幕高度

			// 容器高度设定
			_wap._$('.g-container').style.height = this.viewH + 'px';
			_wap._$('#m-misshome').style.height = this.viewH - 60 + 'px';

		},

		// 交互事件
		_$interactive : function() {

			/**************************************************
			 *事件绑定
			 ****************************************************/

			window.onresize = _wap._$throttle(this._$draw._$bind(this), 100); // 检测屏幕变化渲染页面 (函数节流 delay 100ms)
		}
	}

	// dom ready 加载 渲染页面
	_wap._$domReady(function() {

		homeManager._$draw();
		homeManager._$interactive();

	})
})	
