/*
 * --------------------------------------------
 * xx窗体控件实现
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * --------------------------------------------
 */
define(['base/klass',
        'base/element',
        'base/event',
		'pro/widget/layer/window',
		'text!./sure.window.html',
		'util/template/tpl'],
		function(_k,_e,_v, _BaseWindow,_html,_e1,_p) {
			var pro, sup;
			// ui css text
			var _seed_html= _e1._$addNodeTemplate(_html);
			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$SureWindow}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			_p._$$SureWindow = _k._$klass();
			pro = _p._$$SureWindow._$extend(_BaseWindow);
			sup = _p._$$SureWindow._$supro;
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
				options.clazz +=' w-win w-win-1 w-win-1-3';
				this.__supReset(options);
				this.__tip.innerHTML = options.txt||'确定删除该地址？';
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
				this.__tip = list[0];
				_v._$addEvent(list[1], 'click', this.__onOKClick._$bind(this));
				_v._$addEvent(list[2], 'click', this.__onCCClick._$bind(this));

			};

			pro.__onCCClick = function(event) {
				_v._$stop(event);
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				_v._$stop(event);
				this._$dispatchEvent('onok');
				this._$hide();
			}
			return _p._$$SureWindow;
		})