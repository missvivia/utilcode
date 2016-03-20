/**
 *wap工具函数：
 *设备UA检测+设备环境获取+Transition操作+工具函数+验证API
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
	'{pro}/lib/wap/global.js' // 对应对象g（全局拓展）
], function (g, p, o, f, r) {

	// UA
	var __userAgent = navigator.userAgent;

	// 类型判断
	function __isType(type) {
		return function (obj) {
			return {}.toString.call(obj) == "[object " + type + "]";
		}
	}

	// wap
	var _wap = {
		/*==================================================
		 Helpers
		 ====================================================*/
		/**
		 *类型判断
		 */
		// @return {Boolean}          是否是object
		_$isObject: __isType("Object"),

		// @return {Boolean}          是否是string
		_$isString: __isType("String"),

		// @return {Boolean}          是否是array
		_$isArray: Array.isArray || __isType("Array"),

		// @return {Boolean}          是否是function
		_$isFunction: __isType("Function"),
		/**
		 *设备UA检测
		 */
		// @return {String}           userAgent
		_$userAgent: __userAgent,

		// @return {Boolean}          是否是ios系统
		_$isIOS: !!__userAgent.match(/(iPhone|iPod|iPad)/i),

		// @return {Boolean}          是否是android系统
		_$isAndroid: !!__userAgent.match(/Android/i),

		// @return {Boolean}          是否是Windows Phone
		_$isWphone: !!__userAgent.match(/Windows Phone/i),

		// @return {Boolean}          是否是PC (function防止safari this指向window问题)
		_$isPC: function () {
			return !this._$isIOS && !this._$isAndroid && !this._$isWphone
		},
		/**
		 *window属性
		 */
		// @return {Number}           window可视宽度
		_$getWindowWidth: function () {
			if (window.innerWidth) {
				return window.innerWidth
			} else if (document.documentElement && document.documentElement.clientWidth) {
				return document.documentElement.clientWidth;
			}
		},
		// @return {Number}           window可视高度
		_$getWindowHeight: function () {
			if (window.innerHeight) {
				return window.innerHeight
			}
			else if (document.documentElement && document.documentElement.clientHeight) {
				return document.documentElement.clientHeight;
			}
		},
		// @return {Object}           window scroll位置对象
		_$getWindowScroll: function () {
			return {
				left: document.documentElement.scrollLeft || window.pageXOffset || document.body.scrollLeft,
				top: document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop
			}
		},
		/**
		 *事件监听
		 * @param  {Element}   arg0 - 监听对象
		 * @param  {String}    arg1 - 监听行为
		 * @param  {Function}  arg2 - 执行函数
		 */
		_$addEventListener: function (el, type, fn) {
			if (el.addEventListener) {
				el.addEventListener(type, fn, false);
			} else if (el.attachEvent) {
				el.attachEvent('on' + type, fn);
			} else {
				el['on' + type] = fn;
			}
		},
		/**
		 *domready callback
		 * @param  {Fuction}   arg0 - domready后执行函数
		 */
		_$domReady: function (fn) {
			//IOS and Android
			if (document.addEventListener) {
				document.addEventListener("DOMContentLoaded", fn(), false);
			}
			//Windows Phone
			else if (document.attachEvent) {
				document.attachEvent("onreadystatechange", function () {
					if (document.readyState === "complete") {
						fn();
					}
				});
			}
		},
		/*==================================================
		 Feature Detection
		 ====================================================*/
		_$support: {

			touch: (window.Modernizr && Modernizr.touch === true) || (function () {
				return !!(("ontouchstart" in window) || window.DocumentTouch && document instanceof DocumentTouch);
			})(),

			transforms: (window.Modernizr && Modernizr.csstransforms === true) || (function () {
				var __div = document.createElement('div').style;
				return ('transform' in __div) || ('WebkitTransform' in __div) || ('MozTransform' in __div) || ('msTransform' in __div) || ('MsTransform' in __div) || ('OTransform' in __div);//
			})(),

			transitions: (window.Modernizr && Modernizr.csstransitions === true) || (function () {
				var __div = document.createElement('div').style;
				return ('transition' in __div) || ('WebkitTransition' in __div) || ('MozTransition' in __div) || ('msTransition' in __div) || ('MsTransition' in __div) || ('OTransition' in __div);
			})()
		},
		/*==================================================
		 Transition
		 ====================================================*/
		/**
		 *设定transform
		 * @param  {Element}   arg0 - transform对象
		 * @param  {String}    arg1 - transform动作
		 * @param  {Number}    arg2 - transform执行事件
		 */
		_$setTransform: function (el, action, time) {
			if (!el || !action) {
				return false;
			}
			el.style.transform = el.style.webkitTransform = el.style.mozTransform = el.style.oTransform = el.style.msTransform = action;
			el.style.transitionDuration = el.style.webkitTransitionDuration = el.style.mozTransitionDuration = el.style.oTransitionDuration = el.style.msTransitionDuration = (time / 1000 || 0.1) + 's';
		},
		/**
		 *基于transform矩阵 获取当前translate 动画位置  （平面空间）
		 * @param  {Element}   arg0 - transform对象
		 * @param  {String}    arg1 - 获取的transform属性 x 或 y
		 * @return {Number}           当前位置 单位px
		 */
		_$getTranslatePosition: function (el, axis) {
			var
				matrix, // transform矩阵
				curTransform; // 具体位置
			if (window.WebKitCSSMatrix) {
				var transformMatrix = new WebKitCSSMatrix(window.getComputedStyle(el, null).webkitTransform)
				matrix = transformMatrix.toString().split(',');
			} else {
				var transformMatrix = window.getComputedStyle(el, null).MozTransform || window.getComputedStyle(el, null).OTransform || window.getComputedStyle(el, null).MsTransform || window.getComputedStyle(el, null).msTransform || window.getComputedStyle(el, null).transform || window.getComputedStyle(el, null).getPropertyValue("transform").replace("translate(", "matrix(1, 0, 0, 1,");
				matrix = transformMatrix.toString().split(',');

			}
			if (axis == 'x') {
				// IE10
				if (matrix.length == 16) {
					curTransform = parseFloat(matrix[12]);
				}
				// WebKit
				else if (!!window.WebKitCSSMatrix) {
					curTransform = transformMatrix.m41;
				}
				// 其余高级浏览器
				else {
					curTransform = parseFloat(matrix[4]);
				}
			}
			if (axis == 'y') {
				// IE10
				if (matrix.length == 16) {
					curTransform = parseFloat(matrix[13]);
				}
				// WebKit
				else if (!!window.WebKitCSSMatrix) {
					curTransform = transformMatrix.m42;
				}
				// 其余高级浏览器
				else {
					curTransform = parseFloat(matrix[5]);
				}
			}

			return curTransform || 0;
		},
		/**
		 *transition结束回调函数
		 * @param  {Element}   arg0 - transition对象
		 * @param  {Fuction}   arg1 - transition结束回调函数
		 * @return {Number}           当前位置 单位px
		 */
		_$atTransitionEnd: function (el, callback, permanent) {
			var events = ['webkitTransitionEnd', 'transitionend', 'oTransitionEnd', 'MSTransitionEnd', 'msTransitionEnd'];

			function fireCallBack() {
				callback();
				if (!permanent) {
					for (var i = 0, l = events.length; i < l; i++) {
						el.removeEventListener(events[i], fireCallBack, false)
					}
				}
			}

			// 设置事件  只兼容支持translate的浏览器
			if (!!callback) {
				for (var i = 0, l = events.length; i < l; i++) {
					el.addEventListener(events[i], fireCallBack, false)
				}
			}
		},
		/*==================================================
		 API
		 ====================================================*/
		/**
		 *dom选择器  IE10+ 或jquery  兼容 Wphone(IE 10+)、 Android、 IOS的兼容性
		 * @param  {Selector}  arg0 - css选择器
		 * @return {Element}          唯一的节点元素或节点数组
		 */
		_$: function (selector) {
			if (!document.querySelectorAll) return false;
			var __elems = document.querySelectorAll(selector);
			if (__elems.length == 1) {
				return __elems[0];
			} else {
				return [].slice.call(__elems);
			}
		},
		/**
		 *get url 参数
		 * @param  {String}    arg0 - url key
		 * @return {String}           存在key的value
		 */
		_$getUrlParam: function (key) {
			var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)", "i");
			var result = window.location.search.substr(1).match(reg);
			return !!result ? unescape(result[2]) : null;
		},
		/**
		 *openApp 唤醒app(iframe 防止safari报错"链接无效") 若客户端安装openUrl的app则唤醒app否则什么也不做
		 * @param  {String}    arg0 - App唤醒码
		 */
		_$openApp: function (openUrl) {
			var _ifr = document.createElement('iframe');
			_ifr.src = openUrl;
			_ifr.style.display = 'none';
			document.body.appendChild(_ifr);
			setTimeout(function () {
				document.body.removeChild(_ifr);
			}, 3000)

		},
		/**
		 *函数节流
		 * @param  {Fuction}   arg0 - 回调函数
		 * @param  {Number}    arg1 - 延迟时间
		 */
		_$throttle: function (fn, delay) {
			var timer = null;
			return function () {
				var
					context = this, // this绑定
					args = arguments; // 回调函数参数
				clearTimeout(timer);

				timer = setTimeout(function () {
					fn.apply(context, args);
				}, delay);
			};
		},
		/**
		 *获取用户登录状态
		 * @return {String}           登录状态下的用户名
		 */
		_$getName: function () {
			var info = document.cookie.match(/(^| )P_INFO=([^;]*)(;|$)/i);
			return !!info ? unescape(info[2]).split("|")[0] : null;
		},
		/**
		 *退出登录
		 */
		_$logout: function () {
			var username = this._$getName();
			if (!!username) {
				window.location.href = "http://reg.163.com/Logout.jsp?username=" + username + "&url=" + encodeURIComponent(window.location.href);
			} else {
				return false;
			}
		}
	}
	// change to NEJ module
	return _wap;
})