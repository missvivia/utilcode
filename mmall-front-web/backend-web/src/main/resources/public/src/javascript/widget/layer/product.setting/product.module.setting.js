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
		 , 'text!./countdown.setting.css'
	     , 'text!./countdown.setting.html'
	     , 'base/config'
	     , '{pro}widget/ui/color/color.js'
	     , '{lib}util/template/tpl.js'
		 ],
		function(u, v, e, t, Window,css,html,c,Color, e1,p, o, f, r) {
			var pro,
			_seed_css = e._$pushCSSText(css,{root:c._$get('root')}),
	        _seed_html= e1._$addNodeTemplate(html);


			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$ProductModuleSetting}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$ProductModuleSetting = NEJ.C();
			pro = p._$$ProductModuleSetting._$extend(Window);
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
				options.title = '字体样式设置';
				this.__super(options);
				this.fontColorPick = Color._$allocate({parent:this.fontColor,clazz:'m-fontpick'});
				this.bgColorPick = Color._$allocate({parent:this.bgColor,clazz:'m-bgpick'});
				this.borderColorPick = Color._$allocate({parent:this.borderColor,clazz:'m-bdpick'});
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
				this.borderColorPick =  this.borderColorPick._$recycle();
				this.fontColorPick =  this.fontColorPick._$recycle();
				this.bgColorPick =  this.bgColorPick._$recycle();
				this.__super();
			};
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() {
				this.__super();
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.fontFamily = list[0];
				this.fontWeight = list[1];
				this.fontSize = list[2];
				this.fontColor = list[3];
				this.bgColor = list[4];
				this.bgOpacity = list[5];
				this.borderColor = list[6];
				v._$addEvent(list[7], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[8], 'click', this.__onCCClick._$bind(this));
			};

			pro.__onCCClick = function(event) {
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				var style ={};
//				style.family = this.fontFamily.options[this.fontFamily.selectedIndex].text;
//				style.weight = this.fontWeight.options[this.fontWeight.selectedIndex].value;
//				style.size = this.fontSize.options[this.fontSize.selectedIndex].value;
				
				style.font = 'normal '+this.fontWeight.options[this.fontWeight.selectedIndex].text+' '+
							this.fontSize.options[this.fontSize.selectedIndex].text +' '+
							this.fontFamily.options[this.fontFamily.selectedIndex].text +' '
				style.color = this.fontColorPick._$getColor();
				
				style.bgColor = this.bgColorPick._$getColor();
				style.bgOpcity = parseInt(this.bgOpacity.value)||1;
				style.borderColor = this.borderColorPick._$getColor();
				
				this._$dispatchEvent('onok',font);
				this._$hide();
			}
			return p._$$ProductModuleSetting;
		})