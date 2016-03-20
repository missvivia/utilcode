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
		 ,'{lib}util/template/tpl.js'
		 ,'text!./move.images.html'
		 ,'text!./move.images.css'],
		function(u, v, e, t, Window,e1,html,css, p, o, f, r) {
			var pro, sup,
			_seed_css = e._$pushCSSText(css),
			_seed_html= e1._$addNodeTemplate(html);
			// ui css text

			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$MoveImagesWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$MoveImagesWin = NEJ.C();
			pro = p._$$MoveImagesWin._$extend(Window);
			sup = p._$$MoveImagesWin._$supro;
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
				options.title = '移动图片';
				this.__super(options);
				this.__initSelect(options.categories)
			};
			
			pro.__initSelect = function(list){
				this.select.options.length = 0;
				for(var i=0,l=list.length;i<l;i++){
					var option=  new Option(list[i].name,list[i].id);
					this.select.options.add(option);
				}
			}
			/**
			 * 动态构建控件节点模板
			 * @protected
			 * @method {__initNodeTemplate}
			 * @return {Void}
			 */
			pro.__initNodeTemplate = function() {
				this.__seed_html = _seed_html;
				this.__seed_css  = _seed_css;
			};
			/**
			 * 销毁控件
			 */
			pro.__destroy = function() {
				this.__super();
			};
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() {
				this.__super();
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.select = list[0];
				v._$addEvent(list[1], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[2], 'click', this.__onCCClick._$bind(this));
			};

			pro.__onCCClick = function(event) {
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				this._$dispatchEvent('onok', this.select.options[this.select.selectedIndex].value);
				this._$hide();
			}
			return p._$$MoveImagesWin;
		})