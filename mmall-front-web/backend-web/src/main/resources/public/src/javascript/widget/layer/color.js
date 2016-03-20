/*
 * --------------------------------------------
 * xx卡片控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
		[ '{lib}base/util.js'
		  , '{lib}base/event.js'
		  , '{lib}base/element.js'
		  , '{lib}util/event.js'
		  , '{lib}ui/layer/card.wrapper.js'
		  ,	'{lib}util/template/tpl.js'
		  , '{lib}ui/colorpick/colorpanel.js'],
		function(u, v, e, t, i, e1,ColorPanel,p, o, f, r) {
			var pro, sup;

			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$ColorCard}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$ColorCard = NEJ.C();
			pro = p._$$ColorCard._$extend(i._$$CardWrapper);
			sup = p._$$ColorCard._$supro;


			/**
			 * 控件重置
			 * @protected
			 * @method {__reset}
			 * @param  {Object} options 可选配置参数
			 */
			pro.__reset = function(options) {
				this.__supReset(options);
				this.panel = ColorPanel._$$ColorPanel._$allocate({parent:this.__body,clazz:'m-colorpick',defaultColor:options.defaultColor,onchange:this.__onColorChange._$bind(this)});
			};
			pro._$setColor = function(color){
				this.panel._$setColor(color);
			}
			pro.__onColorChange = function(color){
				this._$dispatchEvent('onchange',color)
			}
			
			/**
			 * 动态构建控件节点模板
			 * @protected
			 * @method {__initNodeTemplate}
			 * @return {Void}
			 */
			pro.__initNodeTemplate = function() {
				var seed_html = e1
						._$addNodeTemplate('<div></div>');
				this.__seed_html = seed_html;
			};
			/**
			 * 销毁控件 
			 */
			pro.__destroy = function() {
				this.__supDestroy();

			};
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() {
				this.__supInitNode();

			};

			pro.__onBodyClick = function(event) {
			}
			return p._$$ColorCard;
		})