/*
 * --------------------------------------------
 * 视频输入窗体控件实现
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * --------------------------------------------
 */
define(
		[ '{lib}base/util.js'
		 ,'{lib}base/event.js'
		 ,'{lib}base/element.js'
		 ,'{lib}util/event.js'
		 ,'{pro}widget/layer/window.js'
		 , 'text!./video.css'
	     , 'text!./video.html'
	     , '{lib}util/template/tpl.js'
	     ,'{pro}/extend/util.js'
		 ],
		function(u, v, e, t,Window,css,html, e1,t1,p, o, f, r) {
			var pro,
			_seed_css = e._$pushCSSText(css,{root:c._$get('root')}),
	        _seed_html= e1._$addNodeTemplate(html);


			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$VideoWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$VideoWin = NEJ.C();
			pro = p._$$VideoWin._$extend(Window);
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
				options.title = '添加视频';
				this.__super(options);
				
			};
			
			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() {
				// 在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
				// this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
				// 这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
				// this.__seed_html = 'wgt-ui-xxx';
				this.__seed_css  = _seed_css;
		        this.__seed_html = _seed_html;
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
				v._$addEvent(list[5], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[6], 'click', this.__onCCClick._$bind(this));
			};
			
			pro.__onCCClick = function(event) {
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				this._$dispatchEvent('onok',data);
				this._$hide();
			}
			return p._$$VideoWin;
		})