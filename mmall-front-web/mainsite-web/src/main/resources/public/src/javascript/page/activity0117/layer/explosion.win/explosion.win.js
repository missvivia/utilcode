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
		'../activity.win.js',
		'text!./explosion.win.html',
		'text!./explosion.win.css',
		'util/template/tpl'],
		function(_k,_e,_v, _BaseWindow,_html,_css,_e1,_p) {
			var pro, sup;
			// ui css text
			var _seed_html= _e1._$addNodeTemplate(_html)
				_seed_css = _e._$pushCSSText(_css);
			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$ExplosionWindow}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			_p._$$ExplosionWindow = _k._$klass();
			pro = _p._$$ExplosionWindow._$extend(_BaseWindow);
			sup = _p._$$ExplosionWindow._$supro;
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
				
				this.__supReset(options);
			};

			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() {
				this.__seed_html = _seed_html;
				this.__seed_css = _seed_css;
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
				_v._$addEvent(list[0], 'click', this.__onOKClick._$bind(this));

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
			return _p._$$ExplosionWindow;
		})