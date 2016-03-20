/**
 *列表更新注入：
 *  主要功能： 到底加载页面注入
 *
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
	'util/ajax/xdr', // 对应对象_j(ajax请求)
	'text!./listPage.html', // 对应对象tpl(页面加载)
	'{pro}/widget/webview/wap.js', // 对应对象_wap(基础工具函数)
	'{pro}/lib/regularjs/dist/regular.js' // 对应对象window Regular(页面注入)
], function(_j, tpl, _wap, p, o, f, r) {
	// 页面注入继承
	var NoteList = Regular.extend({
		template : tpl
	});

	// 商品list对象
	var listPage = {

		// list初始化
		_init : function() {

			// 分页最多请求数
			this.limit = 20;

			// 标记请求起始ID
			this.lastId = 0;

			// 标记：没有更多分页
			this.nomore = false;

			// 请求的PO id
			this.scheduleId = '';

			// 标记正在请求中
			this.isloading = false;
		},

		// 分页加载
		_$show : function() {
			if (!this.nomore && !!this.scheduleId && !this.isloading) {

				this.isloading = true; // 未请求成功,未得到请求结果,地域不满足要求时 锁定请求
				_wap._$('.m-shop-loading').style.display = 'block'; // loading提示显示

				// 请求分页列表数据
				_j._$request("/m/share/po/data?scheduleId=" + this.scheduleId + "&limit=" + this.limit + "&lastId=" + this.lastId, {
					method : 'GET',
					type : "json",
					onload : function(_data) {
						if (!!_data.canAccess) {
							var
								data = _data.result, // 请求数据
								datalist = data.list, // 商品列表
								dl = datalist.length, // 商品列表长度
								skulist, // skulist
								list; // 注入数据

							// 判断是否还能继续翻页
							if (data.hasNext) {
								this.nomore = false;
								this.lastId = datalist[dl - 1].id;
							}else{
								this.nomore = true;
							}

							//判断是否售罄
							for(var i = 0; i < dl; i++){

								var resnum = 0; // 剩余数量统计
								skulist = datalist[i].skuList;

								for(var j = 0, sl = skulist.length; j < sl; j++){

									resnum += skulist[j].state;

									// 售罄state为3 统计是否所有sku商品均售罄
									if(resnum == sl*3){
										datalist[i].saleout = true;
									}else{
										datalist[i].saleout = false;
									}
								}
							}

							_wap._$('.m-shop-loading').style.display = 'none'; // loading提示隐藏
							// 返回regular module
							list = new NoteList({
								data : {
									list : datalist
								}
							}).$inject(".g-shop-list");

							// 加载后图片比例初始化
							var picH = _wap._$('.m-module-img')[0].offsetWidth * 1.25; // 图片高度

							// 设置图片高度
							_wap._$('.m-module-img')._$forEach(function(img){
								img.style.height = picH + 'px';
							});

							// 解除请求锁定
							this.isloading = false;
						}
					}._$bind(this),
					onerror : function(_error) {
						console.log(_error);
					}
				});
			}
		}
	}
	// list初始化
	listPage._init();

	// change to NEJ module
	return listPage;
})