/*
 * --------------------------------------------
 * 图片弹出窗体实现
 * @version  1.0
 * @author   hzzhangweidong(hzzhangweidong@corp.netease.com)
 * --------------------------------------------
 */
define(['base/klass',
        'base/element',
        'base/event',
        'pro/widget/layer/window',
		'text!./zoom.window.html',
		'util/template/tpl'],
		function(_k,_e,_v, _BaseWindow,_html,_e1,_p) {
			var pro, sup;
			// ui css text
			var _seed_html= _e1._$addNodeTemplate(_html);
			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$ZoomWindow}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			_p._$$ZoomWindow= _k._$klass();
			pro = _p._$$ZoomWindow._$extend(_BaseWindow);
			sup = _p._$$ZoomWindow._$supro;
			/**
			 * 控件重置
			 * @protected
			 * @method {__reset}
			 * @param  {Object} options 可选配置参数
			 *                           clazz
			 *                           draggable
			 *                           mask
			 */
			pro.__reset = function(_options) {
				_options.mask=true;
				_options.draggable=true;
				_options.clazz="m-window-bi";
				this.__pic.src=_options.src||"/res/images/logo.png";
				this.__super(_options);
				_e._$removeByEC(this.__layer.__dopt.mbar)
//				.remove();
//				this.__layer.__dopt.mbar.remove();
				
			};
			
			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() {
				this.__seed_html = _seed_html;
			};

			/**
			 * 初使化UI
			 */
			pro.__destory = function() {
				this.__super();
			};
			/**
			 * 初使化节点
			 */
			pro.__initNode = function() {
				this.__super();
				var list = _e._$getByClassName(this.__body, 'j-flag');
				this.__pic=list[0];
				this.__close=list[1];
			};
			return _p._$$ZoomWindow;
		})