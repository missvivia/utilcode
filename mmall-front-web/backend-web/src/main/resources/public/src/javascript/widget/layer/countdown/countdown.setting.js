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
	     , '{pro}widget/ui/color/color.js'
	     , '{lib}util/template/tpl.js'
	     ,'{pro}/extend/util.js'
		 ],
		function(u, v, e, t, Window,css,html,Color, e1,ue,p, o, f, r) {
			var pro,
			_seed_css = e._$pushCSSText(css),
	        _seed_html= e1._$addNodeTemplate(html);


			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$CountDownSettingWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$CountDownSettingWin = NEJ.C();
			pro = p._$$CountDownSettingWin._$extend(Window);
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
				
				var obj = ue.string2Obj(options.cssText,";");
				var font = obj.font;
				if(font){
					list = font.split(' ');
					this.fontWeight = list[0];
					this.fontSize = list[1];
					this.fontFamily = list[2];
				}
				this.color = obj.color;
				this.bgColor = obj['background-color'];
				this.bgOpacity = obj.opacity;
				var list =  obj['border']&&obj['border'].split(' ');
				if(list&&list.length){
					this.borderColor = list[2]||'';
				}
				this.__initData();
				
			};
			pro.__initData = function(){
				this.__initSelect(this.fontFamilySelect,this.fontFamily);
				this.__initSelect(this.fontSizeSelect,this.fontSize);
				this.__initSelect(this.fontWeightSelect,this.fontWeight);
				this.bgOpacityIpt.value = this.bgOpacity||'';
				this.fontColorPick = Color._$allocate({parent:this.fontColorParent,clazz:'m-fontpick',defaultColor:this.color||''});
				this.bgColorPick = Color._$allocate({parent:this.bgColorParent,clazz:'m-bgpick',defaultColor:this.bgColor||''});
				this.borderColorPick = Color._$allocate({parent:this.borderColorParent,clazz:'m-bdpick',defaultColor:this.borderColor||''});
			};
			
			pro.__initSelect = function(select,text){
				var options = select.options,selectedIndex =-1;
				for(var i=0,l=options.length;i<l;i++){
					if(options.text==text){
						selectedIndex = i;break;
					}
				}
				if(selectedIndex!=-1){
					select.selectedIndex = selectedIndex;
				}
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
				this.fontFamilySelect = list[0];
				this.fontWeightSelect = list[1];
				this.fontSizeSelect = list[2];
				this.fontColorParent = list[3];
				this.bgColorParent = list[4];
				this.bgOpacityIpt = list[5];
				this.borderColorParent = list[6];
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
				
				style.font = this.fontWeightSelect.options[this.fontWeightSelect.selectedIndex].text+' '+
							this.fontSizeSelect.options[this.fontSizeSelect.selectedIndex].text +' '+
							this.fontFamilySelect.options[this.fontFamilySelect.selectedIndex].text;
				style.color = this.fontColorPick._$getColor();
				
				style.bgColor = this.bgColorPick._$getColor();
				style.bgOpcity = parseInt(this.bgOpacityIpt.value)||1;
				style.borderColor = this.borderColorPick._$getColor();
				
				this._$dispatchEvent('onok',style);
				this._$hide();
			}
			return p._$$CountDownSettingWin;
		})