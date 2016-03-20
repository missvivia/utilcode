/**
 *分享单品页：
 *  主要功能： 滑动封装 + 轮播图 + 屏幕尺寸变化的参数更新
 *
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
	'{pro}/lib/swiper/swiper.scrollbar.js', // 对应对象Swiper(滑动封装函数)
	'{pro}/page/share/product/detailPage.js', // 对应对象detailPage(详情页)
	'{pro}/lib/wap/countdown.js', //对应对象timer(倒计时)
	'{pro}/lib/wap/openApp.js', // 对应对象app(唤醒App相关函数)
	'{pro}/lib/wap/wap.js' // 对应对象_wap(基础工具函数)
], function(Swiper, detailPage, timer, app, _wap, p, o, f, r) {

	var productManager = {

		// 页面初始化
		_$init : function() {

			/**************************************************
			 * 页面参数初始化
			 ****************************************************/

			this.showpage = 1; // 标记当前页码
			this.hasShow = false; // 标记详情页是否初始化
			this.touchTip = _wap._$('.m-touch-tip'); // 翻页提示区域
			this.backTopButton = _wap._$('.backTop'); // 回顶按钮
			this.loadingTip = _wap._$('.g-detail-loading'); // 页面加载提示

			/**************************************************
			 * 唤醒App初始化
			 ****************************************************/

			app.IphoneOpenUrl = 'mmalliphone://page=2?itemId=' + PRODUCTDETAIL.product.productId; // 唤醒iPhone客户端对应详情页
			app.AndroidOpenUrl = 'mmallandroid://page=2?itemId=' + PRODUCTDETAIL.product.productId; // 唤醒Android客户端对应详情页

			/**************************************************
			 * 滑动组件初始化
			 ****************************************************/

			// page1 滑动封装
			this.pageSlide1 = new Swiper('.sp1', { // 滑动容器
				wrapperClass : "showpage1-wrapper", // 滑动模块
				slideClass : "showpage1-slide", // 滑动内容
				scrollContainer : true, // 开启内部滚动
				mode : 'vertical', // 设定方向：竖直
				scrollbar : {
					container : '.showpage1-scrollbar' // 自定义滚动条容器
				},
				// Swiper 新增方法 touchEnd callback
				scrollCallback : function(_this) {

					// 到底 切换详情页
					if (_this.positions.current < -_this.positions.maxPosition) {

						this.showpage = 2;
						this.changePage();

					}

				}._$bind(this)
			});

			// page2滑动封装
			this.pageSlide2 = new Swiper('.sp2', { // 滑动容器
				wrapperClass : "showpage2-wrapper", // 滑动模块
				slideClass : "showpage2-slide", // 滑动内容
				scrollContainer : true, // 开启内部滚动
				mode : 'vertical', // 设定方向：竖直
				scrollbar : {
					container : '.showpage2-scrollbar' // 自定义滚动条容器
				},
				// Swiper 新增方法 touchEnd callback
				scrollCallback : function(_this) {

					// 到顶 切换详情页
					if (_this.positions.current > 0) {

						this.showpage = 1;
						_wap._$setTransform(this.slide.node1, this.slide.data_transform2, 300);
						_wap._$setTransform(this.slide.node2, this.slide.data_transform2);

					}

					// 回到顶部 按钮显示
					if (_this.positions.end < -100) {

						this.backTopButton.style.display = 'block';

					} else {

						this.backTopButton.style.display = 'none';

					}

				}._$bind(this)
			});

			// page1 轮播图
			this.imgSwiper = new Swiper('.imgshow-container', {// 轮播容器
				wrapperClass : "imgshow-wrapper", // 轮播模块
				slideClass : "imgshow-slide", // 轮播内容
				pagination : '.imgshow-pagination', // 数量提示
				loop : true // 设置循环
			});

			// 开启倒计时
			if (!!PRODUCTDETAIL.end) {
				timer._$start(_wap._$('#m-tab-timer'), PRODUCTDETAIL.end);
			}
		},

		// 页面渲染
		_$draw : function() {

			/**************************************************
			 * 页面参数设置
			 ****************************************************/

			this.viewH = _wap._$getWindowHeight(); // 主视角高度
			this.picH = _wap._$('.g-container').offsetWidth; // 展示图高度(轮播图为正方形,且防止轮播图无效  用g-container获取)

			/**************************************************
			 * 页面变换设置
			 ****************************************************/

			this.slide = {
				node1 : _wap._$('.sp1'), // 下拉翻页 第一页
				node2 : _wap._$('.sp2'), // 下拉翻页 第二页
				data_transform1 : 'translate3d(0px, -' + this.viewH + 'px, 0px)', // 第一页顶部移动到-viewH
				data_transform2 : 'translate3d(0px, 0px, 0px)' // 第二页顶部移动到0
			};

			/**************************************************
			 * 页面元素设置
			 ****************************************************/

			_wap._$('.sp2').style.top = this.viewH + 'px';
			_wap._$('.g-container').style.height = this.viewH + 'px';
			_wap._$('.imgshow-container').style.height = this.picH + 'px';

			// 修正safari转屏操作时  显示断层问题
			if (this.showpage == 2) {

				_wap._$setTransform(this.slide.node1, this.slide.data_transform1);
				_wap._$setTransform(this.slide.node2, this.slide.data_transform1);

			}

			/**************************************************
			 * 自定义滚动条 渲染更新
			 ****************************************************/

			this.imgSwiper.reInit();
			this.imgSwiper.resizeFix();
			this.pageSlide1.reInit();
			this.pageSlide1.resizeFix();
			this.pageSlide2.reInit();
			this.pageSlide2.resizeFix();

		},

		// 交互事件
		_$interactive : function() {

			/**************************************************
			 * 交互函数
			 ****************************************************/
			
			// 尺码助手数组过滤
			if(!!PRODUCTDETAIL.product.helper){
				var 
					orginalArr = PRODUCTDETAIL.product.helper.body, // 标记原二维数组
					rowlen = orginalArr.length, // 标记1维长度
					collen = orginalArr[0].length; // 标记2维长度
				for(var i = 0; i < rowlen; i++){
					var flag = 0; // 标记行中空值数量
					for(var j = 0; j < collen; j++){
						if(orginalArr[i][j] == ""){
							flag++;
						}
					}
					// 标记空行
					if(flag == collen){
						orginalArr[i] = 'empty';
					}
				}
			}
			
			// 加载详情图片
			this.showDetail = function() {

				var
					picList = PRODUCTDETAIL.product.customEditHTML, // 商品图片信息
					picinner = document.createElement('div'), // 商家自定义内容占位div
					regexp = new RegExp(/src="(.*?)"/g), // 匹配详情图片地址
					info = new detailPage({
						data : {
							product : PRODUCTDETAIL.product
						}
					}).$inject(".g-detail-container"); // 参数注入

				// 详情图片注入
				if (!!picList) {

					// 防止商家编辑部分纯文本  加载提示无法隐藏问题
					if (picList.match(regexp) == null) {

						// 加载提示隐藏
						this.loadingTip.style.display = 'none';

					}

					var imgsrc; // 标记匹配出的图片地址
					while (( imgsrc = regexp.exec(picList)) != null) {

						var detailImg = new Image(); // 加载商家编辑图片

						// 更新裁剪图片
						detailImg.src = imgsrc[1] + '?imageView&thumbnail=640x0&quality=95';

						// 裁剪后图片加载
						detailImg.onload = function() {

							// 自定义滚动条长度更新
							this.pageSlide2.reInit();

							// 加载提示隐藏
							this.loadingTip.style.display = 'none';

						}._$bind(this)

					}

					// 商家自定义部分注入
					picList = picList.replace(regexp, 'src="$1?imageView&thumbnail=640x0&quality=95"');
					picinner.innerHTML = picList;
					_wap._$('.g-img-wrap').appendChild(picinner);

					// 注入后滚动条长度更新 （防止商家自定义时出现大段文字介绍时  滚动长度不够的情况）
					this.pageSlide2.reInit()

				} else {

					// 商家未编辑详情
					this.loadingTip.style.display = 'none';
					this.pageSlide2.reInit()

				}

				// 标记详情页已初始化
				this.hasShow = true;

			}._$bind(this)

			// 查看详情页
			this.changePage = function() {

				// 判断是否初始化加载详情页
				if (!this.hasShow) {

					this.showDetail();

				}

				// 翻页
				_wap._$setTransform(this.slide.node1, this.slide.data_transform1);
				_wap._$setTransform(this.slide.node2, this.slide.data_transform1, 300);

			}._$bind(this)

			//回顶操作
			this.backTop = function() {

				this.backTopButton.style.display = 'none'; // 隐藏回顶按钮
				this.pageSlide2.setWrapperTransition(300); // Swiper 内部函数 设定 动画时间
				this.pageSlide2.setWrapperTranslate(0, 0, 0); // Swiper 内部函数 设定  translate3d属性
				this.pageSlide2.allowMomentumBounce = false; // Swiper 新增属性   关闭回弹标记    防止惯性到底回弹过程中 用户点击回顶按钮  失效bug

				//回顶缓动
				setTimeout(function() {

					// 页面标记更新
					this.showpage = 1;

					// 翻页
					_wap._$setTransform(this.slide.node1, this.slide.data_transform2, 300);
					_wap._$setTransform(this.slide.node2, this.slide.data_transform2);

					// 第一页回顶缓动
					this.pageSlide1.setWrapperTransition(300); // Swiper 内部函数 设定动画时间
					this.pageSlide1.setWrapperTranslate(0, 0, 0);// Swiper 内部函数 设定  translate3d属性
					this.pageSlide1.allowMomentumBounce = false; // Swiper 新增属性   关闭回弹标记    防止惯性到底回弹过程中 用户点击回顶按钮  失效bug

				}._$bind(this), 300);

			}._$bind(this)

			/**************************************************
			 * 事件绑定
			 ****************************************************/

			_wap._$addEventListener(this.touchTip, 'click', this.changePage); // 点击 显示详情页 跳转
			_wap._$addEventListener(this.backTopButton, 'click', this.backTop); // 点击 回顶按钮 回顶
			window.onresize = _wap._$throttle(this._$draw._$bind(this), 100); // 检测屏幕变化渲染页面  (函数节流  delay 100ms)

		}
	}

	// dom ready 加载 渲染页面
	_wap._$domReady(function() {

		productManager._$init();
		productManager._$draw();
		productManager._$interactive();
	})

})
