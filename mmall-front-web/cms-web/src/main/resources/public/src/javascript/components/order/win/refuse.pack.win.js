/*
 * --------------------------------------------
 * xx窗体控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
		[ '{lib}base/util.js?v=1.0.0.0'
		 ,'{lib}base/event.js?v=1.0.0.0'
		 ,'{lib}base/element.js?v=1.0.0.0'
		 ,'{lib}util/event.js?v=1.0.0.0'
		 ,'text!./refuse.pack.win.html?v=1.0.0.0'
		 ,'{lib}util/template/tpl.js?v=1.0.0.0'
		 ,'{pro}widget/layer/window.js?v=1.0.0.0'],
		function(u, v, e, t, html, e1, Window, p, o, f, r) {
			var pro,
			_seed_html= e1._$addNodeTemplate(html);

			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$RefusePackWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$RefusePackWin = NEJ.C();
			pro = p._$$RefusePackWin._$extend(Window);
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
				options.title = '设为拒件';
				this.__super(options);
			};
			
			/**
			 * 动态构建控件节点模板
			 * @protected
			 * @method {__initNodeTemplate}
			 * @return {Void}
			 */
			pro.__initNodeTemplate = function() {
				this.__seed_html = _seed_html;
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
				this._$hide();
			}
			return p._$$RefusePackWin;
		})