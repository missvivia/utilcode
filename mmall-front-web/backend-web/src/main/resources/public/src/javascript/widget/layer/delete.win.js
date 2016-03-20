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
		 ,'{pro}widget/layer/window.js'],
		function(u, v, e, t, Window, p, o, f, r) {
			var pro;

			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$CancelWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$CancelWin = NEJ.C();
			pro = p._$$CancelWin._$extend(Window);
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
				options.title = '删除';
				this.__super(options);
			};
			
			/**
			 * 动态构建控件节点模板
			 * @protected
			 * @method {__initNodeTemplate}
			 * @return {Void}
			 */
			pro.__initNodeTemplate = function() {
				this.__seed_html = 'ntp-pass-win';
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
				v._$addEvent(list[1], 'click', this.__onCCClick._$bind(this));
			};

			pro.__onCCClick = function(event) {
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				this._$dispatchEvent('onok');
			}
			return p._$$CancelWin;
		})