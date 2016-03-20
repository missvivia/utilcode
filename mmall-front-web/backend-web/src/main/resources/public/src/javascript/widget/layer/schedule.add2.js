/*
 * --------------------------------------------
 * xx窗体控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
		[ '{lib}base/util.js'
		 ,'{lib}base/event.js'
		 ,'{lib}base/element.js'
		 ,'{lib}util/event.js'
		 ,'{pro}widget/layer/window.js'
		 ,'{lib}util/template/jst.js'],
		function(u, v, e, t, Window,j, p, o, f, r) {
			var pro;

			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$AddUserWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$AddUserWin = NEJ.C();
			pro = p._$$AddUserWin._$extend(Window);
			/**
			 * 控件重置
			 * @protected
			 * @method {__reset}
			 * @param  {Object} options 可选配置参数
			 *                           clazz
			 *                           draggable
			 *                           mask
			 */
			pro.__reset = function(options) {
				options.title = '修改档期商品';
				this.__super(options);
				// 添加模版缓存
			    // 也可以用_$parseTemplate接口批量添加
			    // j._$add('jst-add-window');

			    // 根据模板ID取模板内容
			    // 返回整合数据后的html代码
			    // var _html = j._$get('jst-add-window',{
			    //     productVO:result
			    // });




			    
			};
			
			/**
			 * 动态构建控件节点模板
			 * @protected
			 * @method {__initNodeTemplate}
			 * @return {Void}
			 */
			pro.__initNodeTemplate = function() {
				this.__seed_html = 'jst-add-window';
			};
			/**
			 * 销毁控件
			 */
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() {
				this.__super();
				var list = e._$getByClassName(this.__body, 'j-flag');
				v._$addEvent(list[0], 'click', this.__onOKClick._$bind(this));
				// v._$addEvent(list[1], 'click', this.__onCCClick._$bind(this));
			};

			pro.__onCCClick = function(event) {
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				this._$dispatchEvent('onok', '关闭窗体');
				this._$hide();
			};

			

			return p._$$AddUserWin;
		})