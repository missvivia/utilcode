/*
 * --------------------------------------------
 * xx控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
		[ '{lib}base/util.js'
		  , '{lib}base/event.js'
		  , '{lib}base/element.js'
		  ,	'{lib}util/event.js'
		  , '{lib}ui/base.js'
		  , '{lib}util/animation/easeinout.js'
		  , '{lib}util/template/tpl.js'
		  , '{pro}widget/layer/color.js'
		  , 'text!./color.css'
	      , 'text!./color.html'
	      ,'base/config'
		  ],
		function(u, v, e, t, i,t1,e1,Color, css,html,c,p, o, f, r) {
			var pro,
			_seed_css = e._$pushCSSText(css,{root:c._$get('root')}),
	        _seed_html= e1._$addNodeTemplate(html);
			/**
			 * 全局状态控件
			 * 
			 * @class {nm.i._$$Color}
			 * @extends {nej.ui._$$Abstract}
			 */
			p._$$Color = NEJ.C();
			pro = p._$$Color._$extend(i._$$Abstract);
			sup = p._$$Color._$supro;


			/**
			 * 重置控件
			 * 
			 * @param {[type]}
			 *            options [description]
			 * 
			 */
			pro.__reset = function(options) {
				options.parent = options.parent || document.body;
				this.__super(options);
				this._$setColor(options.defaultColor)
			};
			
			/**
			 * 控件销毁
			 * 
			 */
			pro.__destroy = function() {
				this.__super();
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
			 * 节点初使化
			 * 
			 */
			pro.__initNode = function() {
				this.__super();
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.colordemo = list[0];
				this.pick = list[1];
				this.color = list[2];
				v._$addEvent(this.pick,'click',this.__onPickClick._$bind(this));
				v._$addEvent(this.color,'click',this.__onPickClick._$bind(this));
			};
			
			pro.__onPickClick = function(event){
				v._$stop(event)
				this.colorlayer = Color._$allocate({parent:this.__body,color:this.color.value,defaultColor:this.color.value,clazz:'m-colorpick',onchange:this.__onColorChange._$bind(this)})._$show();
			};
			pro._$getColor = function(){
				return this.color.value;
			};
			pro._$setColor = function(color){
				this.__onColorChange(color);
			};
			pro.__onColorChange = function(color){
				this.color.value = color;
				this.colordemo.style.backgroundColor = color;
			};
			return p._$$Color;
		})