/**
 *分享Po页：
 * 主要功能： 浏览器滚动条监测 + 加载列表
 *
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
	'{pro}/page/webview/share/list/listPage.js', // 对应对象detailPage(详情页)
	'{pro}/widget/webview/countdown.js', //对应对象timer(倒计时)
	'{pro}/widget/webview/openApp.js', // 对应对象app(唤醒App相关函数)
	'{pro}/widget/webview/wap.js' // 对应对象_wap(基础工具函数)
], function(listPage, timer, app, _wap, p, o, f, r) {

	var poManager = {

		// 页面初始化
		_$init : function() {

			/**************************************************
			 * 页面参数初始化
			 ****************************************************/

			this.backTop = _wap._$('.backTop'); // 回顶按钮
			this.shopMargin = 30; // 商品列表的marginTop值
			this.addOffset = 50; // 距离底部addOffset值时加载
			this.showBackTop = 500; // 距离顶部showBackTop值时显示回顶按钮

			/**************************************************
			 * PO信息初始化
			 ****************************************************/

			var listId = _wap._$getUrlParam('scheduleId'); // 专场ID

			// 设置注入信息
			listPage.scheduleId = listId;
			listPage.lastId = PODETAIL.lastId;

			/*唤醒对应专场页的App*/
			app.IphoneOpenUrl = 'mmall://page=1?itemId=' + listId; // 唤醒iPhone客户端对应PO页
			app.AndroidOpenUrl = 'mmallandroid://m.023.baiwandian.cn?page=1&itemId=' + listId; // 唤醒Android客户端对应PO页

			/**************************************************
			 * 页面显示初始化
			 ****************************************************/

			//开启倒计时
			if (!!PODETAIL.end) {
				timer._$start(_wap._$('#m-tab-timer'), PODETAIL.end);
			}
		},

		// 页面渲染
		_$draw : function() {
			
			this.pics = _wap._$('.m-module-img'); // 获取列表所有图片
			this.windowH = _wap._$getWindowHeight(); // 屏幕高度
			this.picH = this.pics[0].offsetWidth * 1.25; // 图片高度

			// 设置图片高度
			this.pics._$forEach(function(img){
				img.style.height = this.picH + 'px';
			}._$bind(this));
		},

		// 交互事件
		_$interactive : function() {

			/**************************************************
			 * 交互函数
			 ****************************************************/

			var scrollTimer = null; // 标记scroll函数是否执行
			this.loadrest = function() {
				var
					scrollTop = _wap._$getWindowScroll().top, //滚动条高度
					totalheight = parseFloat(this.windowH) + parseFloat(scrollTop); //当前页面总高度

				// 标记是否显示回顶按钮
				if (scrollTop > this.showBackTop) {
					this.backTop.style.display = 'block';
				} else {
					this.backTop.style.display = 'none';
				}

				// 标记是否加载下一页
				if (_wap._$('.g-shop').offsetHeight + this.shopMargin <= totalheight + this.addOffset) {

					// 滚动条接近底部时 上滑刷新
					if (!listPage.nomore && PODETAIL.hasNext) {

						// 防止scroll事件执行2次
						scrollTimer = scrollTimer ? null : setTimeout(function() {
							listPage._$show();
						}, 0);

						return true;
					} else {
						// 标记是否显示没有分页提示
						_wap._$('.m-shop-nomore').style.display = 'block';
						return false;
					}
				}
			}._$bind(this)

			// 页面回顶
			this.scrollTop = function() {
				document.documentElement.scrollTop = window.pageYOffset = document.body.scrollTop = 0;
			}

			/**************************************************
			 *事件绑定
			 ****************************************************/

			_wap._$addEventListener(this.backTop, 'click', this.scrollTop); // 监听滚动条
			_wap._$addEventListener(document, 'scroll', this.loadrest); // 触发回顶
			window.onresize = _wap._$throttle(this._$draw._$bind(this), 100); // 检测屏幕变化渲染页面 (函数节流 delay 100ms)

		}
	}

	// dom ready 加载 渲染页面
	_wap._$domReady(function() {

		poManager._$init();
		poManager._$draw();
		poManager._$interactive();

	})
})